package spring.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_COOKIE_PATH = "/api/auth/refresh-token";
    public static final long REFRESH_TOKEN_COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    public ResponseCookie buildRefreshTokenCookie(String value){
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure) // Đổi thành true nếu chạy HTTPS thực tế
                .path(REFRESH_TOKEN_COOKIE_PATH) // Chỉ gửi cookie này khi gọi đúng endpoint refresh
                .maxAge(REFRESH_TOKEN_COOKIE_MAX_AGE) // 7 ngày
                .sameSite("Strict")
                .build();
    return cookie;
    }

    public  ResponseCookie clearRefreshTokenCookie(){
        ResponseCookie clearCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(cookieSecure) // khớp với lúc tạo cookie (đổi true khi production HTTPS)
                .path(REFRESH_TOKEN_COOKIE_PATH)
                .maxAge(0)     // ← maxAge = 0 → browser xóa cookie ngay lập tức
                .sameSite("Strict")
                .build();
        return clearCookie;
    }
}
