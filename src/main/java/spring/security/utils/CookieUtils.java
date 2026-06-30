package spring.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth";
    public static final long REFRESH_TOKEN_COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    public ResponseCookie buildRefreshTokenCookie(String value) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
