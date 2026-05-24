package spring.security.mapper;

import org.mapstruct.Mapper;
import spring.security.dto.response.RoleResponse;
import spring.security.entity.Roles;

@Mapper(componentModel = "spring" ,uses = PermissionMapper.class)
public interface RoleMapper {
    RoleResponse toRoleResponse(Roles roles);
}
