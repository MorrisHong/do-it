package kr.joyful.doit.web.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.web.member.MemberInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    TeamRepository teamRepository;

    @Test
    void basic() throws Exception {

        //given
        Member owner = memberService.findMemberById(1L);
        Team saveTeam = teamRepository.save(Team.create("team A", "this team A", owner, 0));

        BoardCreateRequestDto boardRequestDto = BoardCreateRequestDto.builder()
                .title("spring study")
                .description("스프링 스터디 모임")
                .teamId(saveTeam.getId()).build();

        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        mockMvc.perform(post("/api/board")
                .with(user(userDetail))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequestDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/board/{boardId}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }


}