package main.java.com.metacoding.springv2.core.util;

import com.metacoding.springv2.core.util.JWTUtil;
import com.metacoding.springv2.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JWTProvider {

    private final UserDetailsService userDetailsService;

    public JWTProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWTUtil.HEADER);
        if (bearerToken != null && bearerToken.startsWith(JWTUtil.TOKEN_PREFIX)) {
            return bearerToken.replace(JWTUtil.TOKEN_PREFIX, "");
        }
        return null;
    }

    // 토큰을 검증하고 Authentication 반환
    public Authentication getAuthentication(String token) {
        try {
            User user = JWTUtil.verify(token); // JwtUtil에서 User 객체 생성
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
        } catch (Exception e) {
            return null;
        }
    }

    // 토큰이 유효한지 단순 체크
    public boolean validateToken(String token) {
        try {
            JWTUtil.verify(token); // verify 중 예외 안 나면 유효
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
