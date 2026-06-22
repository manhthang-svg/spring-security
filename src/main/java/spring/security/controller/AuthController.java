package spring.security.controller;

import static spring.security.config.OpenApiConfig.BEARER_AUTH_SCHEME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and token lifecycle APIs")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest req,
                                                HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(req, response)));
    }

    @Operation(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody @Valid RegisterRequest req) {
        return ResponseEntity.ok(ApiResponse.success(authService.register(req)));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request,HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success(authService.getNewRefreshToken(request, response)));
    }
    /**
     * Logout: revoke refresh token trong DB + clear HttpOnly cookie phía client.
     * Yêu cầu Access Token hợp lệ trong Authorization header.
     */
    @Operation(summary = "Logout", security = @SecurityRequirement(name = BEARER_AUTH_SCHEME))
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request,
                                                     HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .code(200)
                .message("Đăng xuất thành công")
                .build());
    }
}
