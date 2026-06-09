package spring.security.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import spring.security.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(Long id);


    Optional<RefreshToken> findByToken(String token);

    String getRefreshTokenFromCookie(HttpServletRequest request);

    RefreshToken verifyExpiration(RefreshToken token);

    /**
     * Xóa refresh token khỏi DB theo giá trị token string.
     * Dùng trong logout flow để revoke session ngay lập tức.
     */
    void deleteByToken(String token);

}
