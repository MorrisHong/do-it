package kr.joyful.doit.web.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.team.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TeamControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserDetailsService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("팀만들기 성공")
    @WithUserDetails(userDetailsServiceBeanName = "memberService", value = "member1@example.com")
    void success_create_team() throws Exception {
        TeamCreateRequestDto dto = new TeamCreateRequestDto("teamA", "this is teamA");

        mockMvc.perform(post("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("중복된 팀이름으로 만드려고할 때 배드리퀘스트 떠야함")
//    @WithUserDetails(userDetailsServiceBeanName = "memberService", value = "member1@example.com")
    void failed_duplicate_team_name() throws Exception {
        //given
        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");

        TeamCreateRequestDto dto = new TeamCreateRequestDto("teamA", "this is teamA");

        mockMvc.perform(post("/api/team")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        //when & then
        TeamCreateRequestDto dto2 = new TeamCreateRequestDto("teamA", "this is another teamA");

        mockMvc.perform(post("/api/team")
                .with(user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("서로 다른 멤버가 같은 팀 이름으로 저장할 때 201떠야함")
    void different_member_duplicate_team_name() throws Exception {
        //given
        UserDetails userDetails1 = memberService.loadUserByUsername("member1@example.com");
        UserDetails userDetails2 = memberService.loadUserByUsername("member2@example.com");

        TeamCreateRequestDto dto = new TeamCreateRequestDto("teamA", "this is teamA");

        mockMvc.perform(post("/api/team")
                .with(user(userDetails1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        //when & then
        TeamCreateRequestDto dto2 = new TeamCreateRequestDto("teamA", "this is another teamA");

        mockMvc.perform(post("/api/team")
                .with(user(userDetails2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());

    }
}