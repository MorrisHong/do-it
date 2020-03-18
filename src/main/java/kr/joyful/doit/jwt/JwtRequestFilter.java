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


        JwtAuthenticationDto authentication = extractAuthenticationFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        String username = extractUsernameFromToken(authentication.getAccessToken());

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtTokenUtil.validateAuthentication(authentication)) {

                if(jwtTokenUtil.isTokenExpired(authentication.getAccessToken())
                    && !jwtTokenUtil.isTokenExpired(authentication.getRefreshToken())) {
                    authentication = jwtTokenUtil.generateToken((MemberInfo) userDetails);
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, authentication ,userDetails.getAuthorities());

                response.addHeader(HttpHeaders.AUTHORIZATION, authentication.createAuthenticationHeaderString());
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

    private JwtAuthenticationDto extractAuthenticationFromHeader(String requestTokenHeader) {
        JwtAuthenticationDto jwtAuthenticationDto;
        if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
            String jwtToken = requestTokenHeader.substring(7);
            jwtAuthenticationDto = extractAuthentication(jwtToken);
        } else {
            throw new BadCredentialsException("Invalid Token");
        }
        return jwtAuthenticationDto;
    }

    private JwtAuthenticationDto extractAuthentication(String jwtToken) {
        try {
            return JwtAuthenticationDto.createAuthenticationFromAuthHeader(jwtToken);
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid Token");
        }
    }
}
