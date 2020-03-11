package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.web.member.MemberInfo;
import org.junit.jupiter.api.Disabled;
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

    @Test
    @DisplayName("토큰 발행 테스트")
    public void generate_token() {
        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        //when
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetails);
        String extractEmail = jwtTokenUtil.getUsernameFromToken(jwtAuthenticationDto.getAccessToken());
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
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetails);
        String extractEmail = jwtTokenUtil.getUsernameFromToken(jwtAuthenticationDto.getRefreshToken());
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(jwtAuthenticationDto.getRefreshToken());

        //then
        assertEquals(email, extractEmail);
        assertTrue(expiration.after(new Date(System.currentTimeMillis()))
                && expiration.before(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000)));
    }


    @Test
    @DisplayName("토큰 유효성 검사 테스트")
    public void validate_token() {
        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        String email2 = "member2@example.com";
        UserDetails unAuthorizedUser = memberService.loadUserByUsername(email2);
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetails);

        //when
        boolean isValid = jwtTokenUtil.validateAuthentication(jwtAuthenticationDto, (MemberInfo) unAuthorizedUser);

        //then
        assertFalse(isValid);
    }
}
