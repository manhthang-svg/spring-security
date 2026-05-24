package spring.security.mapper;

import org.mapstruct.Mapper;
import spring.security.dto.response.PermissionResponse;
import spring.security.entity.Permissions;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(Permissions permissions);
}
