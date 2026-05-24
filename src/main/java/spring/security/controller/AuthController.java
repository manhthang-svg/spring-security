package spring.security.controller;

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
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody @Valid LoginRequest req){

        return ResponseEntity.ok(ApiResponse.success(authService.login(req)));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register (@RequestBody @Valid RegisterRequest req){

        return ResponseEntity.ok(ApiResponse.success(authService.register(req)));
    }
}
