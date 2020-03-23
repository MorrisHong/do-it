package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtAuthenticationGenerator jwtAuthenticationGenerator;

    @Test
    @DisplayName("토큰 발행 테스트")
    public void generate_token() {
        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        //when
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        String extractEmail = jwtTokenUtil.getIdFromToken(jwtAuthenticationDto.getAccessToken());
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(jwtAuthenticationDto.getAccessToken());

        //then
        assertEquals(email, extractEmail);
        assertTrue(expiration.after(new Date(System.currentTimeMillis()))
                && expiration.before(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000)));
    }

    @Test
    @DisplayName("refresh토큰 발행 테스트")
    public void generate_refreshToken() {
        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        //when
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        String extractEmail = jwtTokenUtil.getIdFromToken(jwtAuthenticationDto.getRefreshToken());
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(jwtAuthenticationDto.getRefreshToken());

        //then
        assertEquals(email, extractEmail);
        assertTrue(expiration.after(new Date(System.currentTimeMillis()))
                && expiration.before(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000)));
    }

}
