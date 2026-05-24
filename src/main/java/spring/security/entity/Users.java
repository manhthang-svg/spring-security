package spring.security.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    //@Builder.Default // neu khong co cai nay thi gia tri mac dinh khi su dung builder se bi bypass
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
