package kr.joyful.doit.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.jwt.dto.JwtAuthenticationRequest;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserDetailsService userDetailsService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtAuthenticationGenerator jwtAuthenticationGenerator;

    @Test
    @DisplayName("토큰 요청 테스트")
    public void authenticate() throws Exception {
        //given
        String email = "member1@example.com";
        String password = "1234";

        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        //when then
        String authenticateUrl = "/api/authenticate";
        mockMvc.perform(post(authenticateUrl)
                        .servletPath(authenticateUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andDo(print())
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("토큰 요청 테스트, 요청한 user의 email이 notfound일 경우 400 return")
    public void authenticate_user_not_found_expected_401() throws Exception {
        //given
        String email = "notfound@example.com";
        String password = "1234";

        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        //when then
        String authenticateUrl = "/api/authenticate";
        mockMvc.perform(post(authenticateUrl)
            .servletPath(authenticateUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("토큰 요청 테스트, 실제 user load는 성공했으나 password가 틀리면 401 return")
    public void authenticate_wrong_password_expected_401() throws Exception {
        //given
        String email = "member1@example.com";
        String password = "wrongpassword";

        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        //when then
        String authenticateUrl = "/api/authenticate";
        mockMvc.perform(post(authenticateUrl)
            .servletPath(authenticateUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 요청 테스트, refreshToken으로 새로운 token 재발급")
    public void regenerate_token() throws Exception {
        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);
        JwtAuthenticationDto authentication
                = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);

        //when then
        String authenticateUrl = "/api/re-authenticate";
        mockMvc.perform(post(authenticateUrl)
            .servletPath(authenticateUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + authentication.getRefreshToken()))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("토큰 요청 테스트, 유효한 토큰이 아닐경우 401 return")
    public void invalid_access_refresh_token_expect_401() throws Exception {
        //given
        String email = "member1@example.com";
        UserDetails userDetails = memberService.loadUserByUsername(email);

        //when then
        String authenticateUrl = "/api/re-authenticate";
        mockMvc.perform(post(authenticateUrl)
            .servletPath(authenticateUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer invalid"))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

}