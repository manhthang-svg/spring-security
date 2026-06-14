package spring.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.dto.request.LoginRequest;
import spring.security.dto.request.RegisterRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.UserResponse;
import spring.security.service.AuthService;
import spring.security.service.RefreshTokenService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest req, HttpServletResponse response){

        return ResponseEntity.ok(ApiResponse.success(authService.login(req,response)));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register (@RequestBody @Valid RegisterRequest req){

        return ResponseEntity.ok(ApiResponse.success(authService.register(req)));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request,HttpServletResponse response){
        return ResponseEntity.ok(ApiResponse.success(authService.getNewRefreshToken(request,response)));
    }
}
