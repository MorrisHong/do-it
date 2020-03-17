package kr.joyful.doit.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthenticationDto {

    private String accessToken;
    private String refreshToken;

    public static JwtAuthenticationDto createAuthenticationFromAuthHeader(String authorizationHeader) {
        int p = authorizationHeader.indexOf(":");
        return new JwtAuthenticationDto(authorizationHeader.substring(0, p) , authorizationHeader.substring(p + 1));
    }

    public String createAuthenticationHeaderString() {
        return accessToken + ":" + refreshToken;
    }

}