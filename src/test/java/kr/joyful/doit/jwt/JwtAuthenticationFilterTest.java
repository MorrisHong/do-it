package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil realJwtTokenUtil;

    @Autowired
    JwtAuthenticationGenerator jwtAuthenticationGenerator;

    @Test
    @DisplayName("유효한 jwt token 검사 후 filter 정상적인 chaining")
    public void valid_token_access_expect_ok() throws ServletException, IOException {

        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString());

        //when
        when(jwtTokenUtil.getIdFromToken(jwtAuthenticationDto.getAccessToken())).thenReturn(email);
        when(jwtUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        //then
        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("토큰 없이 or 엉뚱한 토큰으로 접근시 filter에서 BadCredentialsException")
    public void invalid_token_access_expect_ok() throws ServletException, IOException {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String token = "invalid";
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        //when
        when(jwtTokenUtil.getIdFromToken(token)).thenThrow(BadCredentialsException.class);

        //then
        Exception exception = assertThrows(
            BadCredentialsException.class,
            () -> jwtAuthenticationFilter.doFilterInternal(request,response,filterChain)
        );
        assertTrue(exception.getMessage().contains("Invalid Token"));
    }

    @Test
    @DisplayName("유효한 token, valid한 user일 경우 SecurityContextHolder에 UserDetail 저장")
    public void valid_access_save_security_context_holder() throws ServletException, IOException {

        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);
        JwtAuthenticationDto authentication = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authentication.createAuthenticationHeaderString());

        //when
        when(jwtTokenUtil.getIdFromToken(authentication.getAccessToken())).thenReturn(email);
        when(jwtUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtTokenUtil.validateToken(authentication.getAccessToken())).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        //then
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        assertNotNull(principal);
        assertEquals(userDetails, principal);

    }

}