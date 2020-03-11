package kr.joyful.doit.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.web.member.MemberInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private final String secret;

    public JwtTokenUtil(@Value("${DOIT_JWT_SIGNATURE}") String secret) {
        this.secret = secret;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String generateToken(MemberInfo memberInfo, JwtTokenType tokenType) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, memberInfo.getEmail(), tokenType);
    }

    public JwtAuthenticationDto generateToken(MemberInfo memberInfo) {
        return new JwtAuthenticationDto(generateToken(memberInfo, JwtTokenType.AUTH)
                , generateToken(memberInfo, JwtTokenType.REFRESH));
    }

    private String doGenerateToken(Map<String, Object> claims, String username, JwtTokenType tokenType) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenType.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateAuthentication(JwtAuthenticationDto auth, MemberInfo memberInfo) {
        String accessToken = auth.getAccessToken();
        String refreshToken = auth.getRefreshToken();
        if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) return false;
        if(!getUsernameFromToken(accessToken).equals(refreshToken)) return false;
        if(!memberInfo.getEmail().equals(accessToken) || !memberInfo.getEmail().equals(refreshToken)) return false;
        if(isTokenExpired(accessToken) && isTokenExpired(refreshToken)) return false;
        return true;
    }

}

