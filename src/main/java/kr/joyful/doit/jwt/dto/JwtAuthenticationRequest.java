package kr.joyful.doit.jwt.dto;

import lombok.Data;

@Data
public class JwtAuthenticationRequest {

    private String email;
    private String password;

}