package spring.security.security.user;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.security.entity.Roles;
import spring.security.entity.Users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {
    private final Users users;

    public CustomUserDetails(Users users) {
        this.users = users;
    }

    /**
     * Trả về {@link Users} entity gốc — dùng cho {@code AuditorAware}
     * để điền {@code createdBy} / {@code updatedBy} tự động.
     */
    public Users getUser() {
        return users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Roles role : this.users.getRoles()) {
            // 1. Thêm bản thân Role đó vào (ví dụ: ROLE_ADMIN)
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            // 2. Thêm tất cả các Permission thuộc Role đó vào (ví dụ: user:create)
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        }

        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !users.getAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !users.getAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return users.getEnabled();
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "users=" + users +
                '}';
    }
}
