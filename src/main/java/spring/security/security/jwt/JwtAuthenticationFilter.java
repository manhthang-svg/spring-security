package spring.security.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.security.security.user.CustomUserDetailsService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils JwtUtils; // Class tự viết để mã hóa/giải mã JWT
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Trích xuất JWT Token từ Header
        String jwt = getJwtFromRequest(request);

        // Nếu không có token, cho request đi tiếp sang các filter khác (ví dụ qua form login công khai)
        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Giải mã lấy username và kiểm tra tính hợp lệ của token
        String username = JwtUtils.extractUsername(jwt);

        // SecurityContextHolder.getContext().getAuthentication() == null nghĩa là 
        // User này chưa từng được xác thực trong request hiện tại
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 3. Lấy thông tin chi tiết của User cùng chùm chìa khóa Roles/Permissions từ DB lên
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Kiểm tra xem token có khớp với thông tin user và chưa hết hạn không
            if (JwtUtils.isTokenValid(jwt, userDetails)) {

                // 4. Tạo "Thẻ chứng nhận đăng nhập thành công" 
                // Quan trọng nhất: Truyền userDetails.getAuthorities() vào đối số thứ 3 để nạp quyền
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Gắn thêm thông tin về request (IP, Session...) vào token bảo mật
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // CHÍNH THỨC xác thực thành công: Đút thẻ chứng nhận vào Ngữ cảnh bảo mật của Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Chuyển giao request cho bộ lọc tiếp theo trong chuỗi FilterChain
        filterChain.doFilter(request, response);
    }

    // Hàm phụ trợ tách chuỗi "Bearer token_string" thành "token_string"
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}