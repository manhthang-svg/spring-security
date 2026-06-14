package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import spring.security.entity.RefreshToken;
import spring.security.entity.Users;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteById(Long id);

    @Modifying

    void deleteByUsers(Users users);

    @Modifying
    void deleteByToken(String token);
}
