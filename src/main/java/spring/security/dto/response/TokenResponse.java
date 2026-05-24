package spring.security.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
}
