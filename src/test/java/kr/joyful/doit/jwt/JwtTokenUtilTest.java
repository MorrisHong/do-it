package kr.joyful.doit.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("토큰 발행 테스트")
    public void generate_token() {
        //given
        String email = "test@gmail.com";
        String password = "password";
        UserDetails userDetails = new User(email, password, Collections.emptyList());

        //when
        String token = jwtTokenUtil.generateToken(userDetails, JwtTokenType.AUTH);
        String extractEmail = jwtTokenUtil.getUsernameFromToken(token);
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

        //then
        assertNotNull(token);
        assertEquals(email, extractEmail);
        assertTrue(expiration.after(new Date(System.currentTimeMillis()))
                && expiration.before(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000)));
    }

    @Test
    @DisplayName("refresh토큰 발행 테스트")
    public void generate_refreshToken() {
        //given
        String email = "test@gmail.com";
        String password = "password";
        UserDetails userDetails = new User(email, password, Collections.emptyList());

        //when
        String token = jwtTokenUtil.generateToken(userDetails, JwtTokenType.REFRESH);
        String extractEmail = jwtTokenUtil.getUsernameFromToken(token);
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

        //then
        assertNotNull(token);
        assertEquals(email, extractEmail);
        assertTrue(expiration.after(new Date(System.currentTimeMillis()))
                && expiration.before(new Date(System.currentTimeMillis() + 3 * 60 * 60 * 1000)));
    }


    @Test
    @DisplayName("토큰 유효성 검사 테스트")
    public void validate_token() {
        //given
        String targetEmail = "test@gmail.com";
        String password = "password";
        UserDetails userDetails = new User(targetEmail, password, Collections.emptyList());
        UserDetails unAuthorizedUser = new User("unAuthorizedUser@gmail.com", "password", Collections.emptyList());
        String token = jwtTokenUtil.generateToken(userDetails, JwtTokenType.AUTH);

        //when
        boolean isValid = jwtTokenUtil.validateToken(token, unAuthorizedUser);

        //then
        assertFalse(isValid);
    }
}
