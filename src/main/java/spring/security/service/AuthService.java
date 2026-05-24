package spring.security.service;

import org.springframework.stereotype.Service;
import spring.security.dto.request.LoginRequest;
import spring.security.dto.request.RegisterRequest;
import spring.security.dto.response.TokenResponse;
import spring.security.dto.response.UserResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);

    UserResponse register(RegisterRequest request);
}
