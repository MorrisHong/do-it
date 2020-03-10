package kr.joyful.doit.jwt;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void setup() {
//        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("유효한 jwt token 검사 후 filter 정상적인 chaining")
    public void authenticated_user_access_endpoint() throws ServletException, IOException {

        //given
        String email = "test@gmail.com";
        String password = "password";
        String username = "choi";

        memberService.join(Member.create(email, username, password, MemberRole.MEMBER));
        UserDetails userDetails = new User(email, password, Collections.emptyList());

        JwtTokenUtil realJwtTokenUtil = new JwtTokenUtil();
        String token = realJwtTokenUtil.generateToken(userDetails, JwtTokenType.AUTH);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + "token");

        //when
        when(jwtTokenUtil.getUsernameFromToken("token")).thenReturn(email);
        when(jwtUserDetailsService.loadUserByUsername(email)).thenReturn(new User(email,password,Collections.emptyList()));

        //then
        jwtRequestFilter.doFilterInternal(request,response,filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}