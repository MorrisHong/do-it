package kr.joyful.doit.web.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.config.RestDocsConfiguration;
import kr.joyful.doit.domain.team.TeamRepository;
import kr.joyful.doit.jwt.JwtAuthenticationGenerator;
import kr.joyful.doit.jwt.JwtTokenType;
import kr.joyful.doit.jwt.JwtTokenUtil;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.web.member.MemberInfo;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
@Transactional
class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserDetailsService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtAuthenticationGenerator jwtAuthenticationGenerator;

    @Test
    @DisplayName("팀만들기 성공")
//    @WithUserDetails(userDetailsServiceBeanName = "memberService", value = "member1@example.com")
    void success_create_team() throws Exception {
        //given
        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);


        //when && then
        TeamCreateRequestDto dto = new TeamCreateRequestDto("test team A", "this is teamA");

        mockMvc.perform(post("/api/team")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document(
                        "create-team",
                        requestFields(
                                fieldWithPath("teamName").description("만들 팀의 이름"),
                                fieldWithPath("description").description("팀에 대한 설명")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증토큰")
                        ))
                );

    }

    @Test
    @DisplayName("중복된 팀이름으로 만드려고할 때 배드리퀘스트 떠야함")
//    @WithUserDetails(userDetailsServiceBeanName = "memberService", value = "member1@example.com")
    void failed_duplicate_team_name() throws Exception {
        //given
        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        TeamCreateRequestDto dto = new TeamCreateRequestDto("test team A", "this is teamA");

        mockMvc.perform(post("/api/team")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
//
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        //when & then
        TeamCreateRequestDto dto2 = new TeamCreateRequestDto("test team A", "this is another teamA");

        mockMvc.perform(post("/api/team")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
//
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("서로 다른 멤버가 같은 팀 이름으로 저장할 때 201떠야함")
    void different_member_duplicate_team_name() throws Exception {
        //given
        UserDetails userDetails1 = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto1 = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails1);


        UserDetails userDetails2 = memberService.loadUserByUsername("member2@example.com");
        JwtAuthenticationDto jwtAuthenticationDto2 = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails2);

        TeamCreateRequestDto dto = new TeamCreateRequestDto("test team A", "this is teamA");

        mockMvc.perform(post("/api/team")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto1.createAuthenticationHeaderString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        //when & then
        TeamCreateRequestDto dto2 = new TeamCreateRequestDto("test team A", "this is another teamA");

        mockMvc.perform(post("/api/team")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto2.createAuthenticationHeaderString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());

    }
}