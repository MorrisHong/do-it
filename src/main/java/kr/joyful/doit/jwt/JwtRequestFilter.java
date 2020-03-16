package kr.joyful.doit.jwt;

import kr.joyful.doit.web.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final List<String> EXCLUDE_URL =
            Arrays.asList("/api/member" , "/api/authenticate");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        String username = extractUsernameFromToken(jwtToken);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtTokenUtil.validateToken(jwtToken, (MemberInfo) userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null ,userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        /**
         * todo 토큰에 대한 확실한 검증 필요
         * 1. access 토큰이 만료되었다면 refresh token의 만료시간까지 체크하고 둘다 만료일 경우 다시 로그인하도록 처리
         * 2. 토큰에 대한 signature 등에 대한 원본 검증 원본 검증 안될시 BadCredentialsException
         * 3. 등등 ..
         */

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }

    private String extractUsernameFromToken(String token) {
        String username;
        try {
            username = jwtTokenUtil.getUsernameFromToken(token);
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid Token");
        }
        return username;
    }

    private String extractTokenFromHeader(String requestTokenHeader) {
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        return jwtToken;
    }
}
