package spring.security.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.entity.RefreshToken;
import spring.security.entity.Users;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.repository.RefreshTokenRepository;
import spring.security.repository.UserRepository;
import spring.security.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${security.jwt.refreshExpirationMs}")
    private Long refreshTokenExpiration;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public RefreshToken generateRefreshToken(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Xóa token cũ trước khi cấp mới → tránh token accumulation
        refreshTokenRepository.deleteByUsers(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .users(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
    @Override
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        // Nếu thời gian hết hạn nhỏ hơn thời gian hiện tại -> Token đã chết
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token); // Xóa luôn khỏi DB cho sạch dữ liệu
            throw new AppException(ErrorCode.REFRESTOKEN_EXPIRED);
        }
        return token;
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
