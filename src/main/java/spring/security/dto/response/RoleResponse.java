package spring.security.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoleResponse {
    private String name;
    private Set<PermissionResponse> permissions;
}