package spring.security.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "username cant blank")
    @Email
    private String username;
    @NotBlank(message = "password cant blank")
    @Size(min = 8, message = "password must be more than 8 characters")
    private String password;
}
