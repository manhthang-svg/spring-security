package spring.security.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.dto.request.LoginRequest;
import spring.security.dto.request.RegisterRequest;
import spring.security.dto.response.TokenResponse;
import spring.security.dto.response.UserResponse;
import spring.security.entity.RefreshToken;
import spring.security.entity.Roles;
import spring.security.entity.Users;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.mapper.UserMapper;
import spring.security.repository.RoleRepository;
import spring.security.repository.UserRepository;
import spring.security.security.jwt.JwtUtils;
import spring.security.security.user.CustomUserDetails;
import spring.security.service.AuthService;
import org.springframework.security.core.GrantedAuthority;
import spring.security.service.RefreshTokenService;

import java.sql.Ref;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserMapper userMapper, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
    }
    @Override
    public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),loginRequest.getPassword()
        ));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal() ;
        Map<String, Object> extraClaims = new HashMap<>();

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extraClaims.put("authorities", authorities);
        String jwt = jwtUtils.generateToken(extraClaims,userDetails.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDetails.getUser().getId());
        // 4. Đưa Refresh Token vào HttpOnly Cookie để bảo mật chống XSS
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false) // Đổi thành true nếu chạy HTTPS thực tế
                .path("/auth/refresh-token") // Chỉ gửi cookie này khi gọi đúng endpoint refresh
                .maxAge(7 * 24 * 60 * 60) // 7 ngày
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new TokenResponse(jwt);
    }
    @Override
    public UserResponse register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);

        Roles userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);

        Users users = Users.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(users);

    return userMapper.toUserResponse(users);
    }
    @Override
    public TokenResponse getNewRefreshToken(HttpServletRequest request,HttpServletResponse response) {
        // 1. Lấy Refresh Token từ Cookie
        String oldToken = refreshTokenService.getRefreshTokenFromCookie(request);
        // 2. Tìm token trong db + check ton tai , han su dung
        RefreshToken refreshTokenIndb = refreshTokenService.findByToken(oldToken).orElseThrow(()-> new AppException(ErrorCode.REFRESHTOKEN_NOT_FOUND));
        refreshTokenService.verifyExpiration(refreshTokenIndb);
        // 3. Xóa token cũ
        refreshTokenService.deleteByToken(oldToken);
        // 4. Tạo access,refresh token mới
        Users users = refreshTokenIndb.getUsers();
        var claims = jwtUtils.getClaims(users.getUsername());
        String newAccessToken = jwtUtils.generateToken(claims,users.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(users.getId());
        // 4. Set cookie mới
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false) // Đổi thành true nếu chạy HTTPS thực tế
                .path("/api/auth/refresh") // Chỉ gửi cookie này khi gọi đúng endpoint refresh
                .maxAge(7 * 24 * 60 * 60) // 7 ngày
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new TokenResponse(newAccessToken);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. Lấy refresh token từ HttpOnly Cookie
        String tokenValue = refreshTokenService.getRefreshTokenFromCookie(request);

        // 2. Nếu có token → xóa khỏi DB để revoke session
        if (tokenValue != null) {
            refreshTokenService.deleteByToken(tokenValue);
        }

        // 3. Xóa cookie phía client bằng cách set maxAge = 0
        //    (Phải giữ nguyên path/domain để browser nhận diện đúng cookie cần xóa)
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false) // khớp với lúc tạo cookie (đổi true khi production HTTPS)
                .path("/api/auth/refresh")
                .maxAge(0)     // ← maxAge = 0 → browser xóa cookie ngay lập tức
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
        log.info("User logged out, refresh token revoked.");
    }
}

