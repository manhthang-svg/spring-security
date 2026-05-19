package spring.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import spring.security.dto.request.LoginRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.entity.Users;
import spring.security.repository.UserRepository;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest req){

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                ));

        return ResponseEntity.ok(ApiResponse.success(auth));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register (@RequestBody LoginRequest req){
        Users users = Users.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();
        userRepository.save(users);
        return ResponseEntity.ok(ApiResponse.success("Register success"));
    }
}
