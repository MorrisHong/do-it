package kr.joyful.doit.web.cardList;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.jwt.JwtTokenType;
import kr.joyful.doit.jwt.JwtTokenUtil;
import kr.joyful.doit.web.member.MemberInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CardListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserDetailsService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Test
    void basic() throws Exception {

        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");
        String token = jwtTokenUtil.generateToken((MemberInfo) userDetails, JwtTokenType.AUTH);
        CardListAddRequestDto cardListDto = CardListAddRequestDto.builder()
                .boardId(1L)
                .name("to do")
                .position(0)
                .build();

        mockMvc.perform(post("/api/card-list")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .with(user(userDetails))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cardListDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}