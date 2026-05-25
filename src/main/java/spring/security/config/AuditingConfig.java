package spring.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import spring.security.entity.Users;
import spring.security.security.user.CustomUserDetails;

import java.util.Optional;

/**
 * Cấu hình JPA Auditing.
 *
 * <p>Bật tính năng tự động điền {@code @CreatedBy} và {@code @LastModifiedBy}
 * trên {@code AbstractEntity} bằng cách cung cấp một {@link AuditorAware} bean
 * đọc user đang đăng nhập từ {@link SecurityContextHolder}.</p>
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfig {

    /**
     * Trả về {@link Users} entity của người dùng đang thực hiện request.
     *
     * <ul>
     *   <li>Nếu chưa xác thực (anonymous / public endpoint) → {@code Optional.empty()},
     *       các field {@code createdBy} / {@code updatedBy} sẽ là {@code null}.</li>
     *   <li>Nếu đã xác thực qua JWT → trả về {@link Users} từ {@link CustomUserDetails}.</li>
     * </ul>
     */
    @Bean
    public AuditorAware<Users> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Chưa đăng nhập hoặc là anonymous request
            if (authentication == null
                    || !authentication.isAuthenticated()
                    || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
                return Optional.empty();
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(userDetails.getUser());
        };
    }
}
