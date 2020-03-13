package kr.joyful.doit.web.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import kr.joyful.doit.jwt.JwtTokenType;
import kr.joyful.doit.jwt.JwtTokenUtil;
import kr.joyful.doit.service.member.MemberService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    JwtTokenUtil jwtTokenUtil;

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
        String token = jwtTokenUtil.generateToken((MemberInfo) userDetail, JwtTokenType.AUTH);

        String boardUrl = "/api/board";
        mockMvc.perform(post(boardUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .with(user(userDetail))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boardRequestDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(boardUrl+ "/{boardId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void inviteMemberInBoard() throws Exception {

        //given
        Member owner = memberService.findMemberById(1L);
        Team saveTeam = teamRepository.save(Team.create("team A", "this team A", owner, 0));

        BoardCreateRequestDto boardRequestDto = BoardCreateRequestDto.builder()
                .title("spring study")
                .description("스프링 스터디 모임")
                .teamId(saveTeam.getId()).build();

        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        String token = jwtTokenUtil.generateToken((MemberInfo) userDetail, JwtTokenType.AUTH);

        String boardUrl = "/api/board";
        mockMvc.perform(post(boardUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .with(user(userDetail))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boardRequestDto)))
                .andExpect(status().isCreated());

        //then
        Member willInviteMember = Member.builder()
                .email("invite@gmail.com")
                .username("invite")
                .role(MemberRole.MEMBER)
                .password("123456")
                .build();
        Long inviteMemberId = memberService.join(willInviteMember);

        mockMvc.perform(put(boardUrl+ "/{boardId}/member/{memberId}", 1L, inviteMemberId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .with(user(userDetail))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get(boardUrl+ "/{boardId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .with(user(userDetail))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findMyBoardList() throws Exception {

        //given
        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        String token = jwtTokenUtil.generateToken((MemberInfo) userDetail, JwtTokenType.AUTH);

        String boardUrl = "/api/board";

        //then
        mockMvc.perform(get(boardUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .with(user(userDetail))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}