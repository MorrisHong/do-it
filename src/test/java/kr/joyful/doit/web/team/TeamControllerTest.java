package kr.joyful.doit.web.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.team.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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
    TeamRepository teamRepository;

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
    @WithUserDetails(userDetailsServiceBeanName = "memberService", value = "member1@example.com")
    void failed_duplicate_team_name() throws Exception {
        //given
        TeamCreateRequestDto dto = new TeamCreateRequestDto("teamA", "this is teamA");

        mockMvc.perform(post("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        //when & then
        TeamCreateRequestDto dto2 = new TeamCreateRequestDto("teamA", "this is another teamA");

        mockMvc.perform(post("/api/team")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest());

    }
}