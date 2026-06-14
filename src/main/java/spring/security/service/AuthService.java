package spring.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import spring.security.dto.request.LoginRequest;
import spring.security.dto.request.RegisterRequest;
import spring.security.dto.response.TokenResponse;
import spring.security.dto.response.UserResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest, HttpServletResponse response);

    UserResponse register(RegisterRequest request);


    TokenResponse getNewRefreshToken(HttpServletRequest request,HttpServletResponse response);


    void logout(HttpServletRequest request, HttpServletResponse response);
}
