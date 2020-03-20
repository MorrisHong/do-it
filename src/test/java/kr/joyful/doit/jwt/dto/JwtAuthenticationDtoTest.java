package kr.joyful.doit.jwt.dto;

import kr.joyful.doit.jwt.JwtAuthenticationGenerator;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class JwtAuthenticationDtoTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    JwtAuthenticationGenerator jwtAuthenticationGenerator;


    @Test
    @DisplayName("header에서 accessToken과 refreshToken 분리하기")
    public void token_extract_from_header_string() {

        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        String headerString ="";

        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);

        headerString += jwtAuthenticationDto.createAuthenticationHeaderString();

        //when
        JwtAuthenticationDto authentication = JwtAuthenticationDto.createAuthenticationFromAuthHeader(headerString);

        //then
        assertEquals(jwtAuthenticationDto.getAccessToken(), authentication.getAccessToken());
        assertEquals(jwtAuthenticationDto.getRefreshToken(), authentication.getRefreshToken());

    }
}