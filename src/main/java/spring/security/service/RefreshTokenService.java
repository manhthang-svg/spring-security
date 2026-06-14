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

    void deleteByToken(String token);

}
