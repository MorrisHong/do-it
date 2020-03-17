package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
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
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final List<String> EXCLUDE_URL =
            Arrays.asList("/api/member" , "/api/authenticate");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        JwtAuthenticationDto authentication = JwtAuthenticationDto.createAuthenticationFromAuthHeader(
                                                                                request.getHeader(HttpHeaders.AUTHORIZATION)
                                                                                        .replace(TOKEN_PREFIX, ""));

        String username = extractUsernameFromToken(authentication.getAccessToken());

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtTokenUtil.validateAuthentication(authentication, (MemberInfo) userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null ,userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

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
        if (requestTokenHeader != null) {
            jwtToken = requestTokenHeader.substring(7);
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        return jwtToken;
    }
}
