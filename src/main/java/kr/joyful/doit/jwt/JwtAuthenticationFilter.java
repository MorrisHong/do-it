package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final List<String> EXCLUDE_URL =
            Arrays.asList("/api/member" , "/api/authenticate");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        JwtAuthenticationDto authentication = extractAuthenticationFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        String email = extractIdFromToken(authentication.getAccessToken());

        if(email != null
                && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtTokenUtil.validateAuthentication(authentication)){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, authentication ,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }

    private String extractIdFromToken(String token) {
        String username;
        try {
            username = jwtTokenUtil.getIdFromToken(token);
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid Token");
        }
        return username;
    }

    private JwtAuthenticationDto extractAuthenticationFromHeader(String requestTokenHeader) {
        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
            String jwtToken = requestTokenHeader.substring(7);
            return extractAuthentication(jwtToken);
        } else {
            throw new BadCredentialsException("Invalid Token");
        }
    }

    private JwtAuthenticationDto extractAuthentication(String jwtToken) {
        try {
            return JwtAuthenticationDto.createAuthenticationFromAuthHeader(jwtToken);
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid Token");
        }
    }
}
