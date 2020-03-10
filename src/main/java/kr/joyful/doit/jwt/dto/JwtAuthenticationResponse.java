package kr.joyful.doit.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;
    private String refreshToken;

}