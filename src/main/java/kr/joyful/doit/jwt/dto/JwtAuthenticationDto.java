package kr.joyful.doit.jwt.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtAuthenticationDto {

    private String accessToken;
    private String refreshToken;

    public static JwtAuthenticationDto createAuthenticationFromTokens(String accessToken, String refreshToken) {
        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        jwtAuthenticationDto.accessToken = accessToken;
        jwtAuthenticationDto.refreshToken = refreshToken;
        return jwtAuthenticationDto;
    }
}