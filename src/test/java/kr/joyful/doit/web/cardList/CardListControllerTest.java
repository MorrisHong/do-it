package kr.joyful.doit.web.cardList;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.config.RestDocsConfiguration;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.jwt.JwtTokenType;
import kr.joyful.doit.jwt.JwtTokenUtil;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.web.member.MemberInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
    void create() throws Exception {

        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetails);
        CardListAddRequestDto cardListDto = CardListAddRequestDto.builder()
                .boardId(1L)
                .name("to do")
                .position(0)
                .build();

        mockMvc.perform(post("/api/card-list")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                    .with(user(userDetails))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cardListDto)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document(
                        "create-card-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type header")
                        ),
                        requestFields(
                                fieldWithPath("boardId").description("카드 리스트가 속할 board의 아이디"),
                                fieldWithPath("name").description("카드 리스트의 이름"),
                                fieldWithPath("position").description("카드 리스트의 위치")
                        )
                ));
    }
}