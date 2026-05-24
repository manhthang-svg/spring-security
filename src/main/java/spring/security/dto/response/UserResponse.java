package spring.security.dto.response;

import lombok.Builder;
import lombok.Data;
import spring.security.entity.Roles;

import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String username;
    private Set<RoleResponse> roles;
}