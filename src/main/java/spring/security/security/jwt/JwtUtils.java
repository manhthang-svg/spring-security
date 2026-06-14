package spring.security.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spring.security.security.user.CustomUserDetails;
import spring.security.security.user.CustomUserDetailsService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtils {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtUtils(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // 1. Lấy Username từ JWT Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Trích xuất một thông tin cụ thể (Claim) bất kỳ từ Token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 3. Tạo Token mới chỉ từ thông tin UserDetails (Khi đăng nhập thành công)
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    // 4. Tạo Token nâng cao (Có thể nhét thêm thông tin tùy chỉnh như Email, ID vào extraClaims)
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey()) // Ký bằng khóa bí mật
                .compact();
    }

    // 5. Kiểm tra Token có hợp lệ không (Khớp username và chưa hết hạn)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Kiểm tra Token đã hết hạn chưa
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Lấy ngày hết hạn từ token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Giải mã toàn bộ token để lấy ra các Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Đọc và xác thực bằng khóa bí mật
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Chuyển chuỗi Secret Key dạng chữ thành mã hóa SecretKey của Java Crypto
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Map<String, Object> getClaims (String username){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Map<String, Object> extraClaims = new HashMap<>();
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        extraClaims.put("authorities", authorities);
        return extraClaims;
    }

}