package spring.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.AccessType;
import spring.security.enums.Role;

@Entity

@Getter
@Setter
@Builder

@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users extends AbstractEntity{
    @Email
    @NotBlank
    @Column(nullable = false,unique = true)
    private String username;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Builder.Default // neu khong co cai nay thi gia tri mac dinh khi su dung builder se bi bypass
    private Role role = Role.USER;
}
