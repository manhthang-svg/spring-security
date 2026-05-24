package spring.security.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
    private String name;
    private String description;
    // Tuyệt đối không chứa danh sách Roles ở đây nữa để chặn vòng lặp!
}