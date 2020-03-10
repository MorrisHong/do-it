package kr.joyful.doit.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.jwt.dto.JwtAuthenticationRequest;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class JwtAuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("/authenticate 요청 테스트")
    public void authenticate() throws Exception {
        //given
        String email = "test@gmail.com";
        String password = "password";
        String username = "choi";
        memberService.join(Member.create(email, username, password, MemberRole.MEMBER));

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

}