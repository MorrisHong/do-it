package kr.joyful.doit.web.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.config.RestDocsConfiguration;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import kr.joyful.doit.jwt.JwtTokenType;
import kr.joyful.doit.jwt.JwtTokenUtil;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.web.member.MemberInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
        Team saveTeam = teamRepository.save(Team.create("teamA", "this team A", owner, 0));

        BoardCreateRequestDto boardRequestDto = BoardCreateRequestDto.builder()
                .title("spring study")
                .description("스프링 스터디 모임")
                .teamId(saveTeam.getId()).build();

        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetail);

        String boardUrl = "/api/board";
        mockMvc.perform(post(boardUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                .with(user(userDetail))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequestDto)))
                .andExpect(status().isCreated())

                .andDo(
                        document(
                                "create-board",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type header")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("보드의 이름"),
                                        fieldWithPath("description").description("보드의 설명"),
                                        fieldWithPath("teamId").description("보드가 속한 팀의 아이디")
                                )
                        )
                );
    }

    @Test
    void inviteMemberInBoard() throws Exception {

        //given
        Member owner = memberService.findMemberById(1L);
        Team saveTeam = teamRepository.save(Team.create("teamA", "this team A", owner, 0));

        BoardCreateRequestDto boardRequestDto = BoardCreateRequestDto.builder()
                .title("jpa study")
                .description("jpa 스터디 모임")
                .teamId(saveTeam.getId()).build();

        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetail);

        String boardUrl = "/api/board";
        mockMvc.perform(post(boardUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
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

        mockMvc.perform(RestDocumentationRequestBuilders.put(boardUrl + "/{boardId}/member/{memberId}", 1L, inviteMemberId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                .with(user(userDetail))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(
                        document(
                                "invite-member",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                                ),
                                pathParameters(
                                        parameterWithName("boardId").description("보드의 아이디"),
                                        parameterWithName("memberId").description("초대하려는 멤버의 아이디")
                                )
                        )
                );
    }

    @Test
    void findMyBoardList() throws Exception {

        //given
        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetail);

        String boardUrl = "/api/board";

        //then
        mockMvc.perform(RestDocumentationRequestBuilders.get(boardUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                .with(user(userDetail))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "retrieve-my-board",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증토큰"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                        ),
                        responseFields(
                                fieldWithPath("myBoard").description("루트 경로"),
                                fieldWithPath("myBoard[].teamId").description("팀의 아이디"),
                                fieldWithPath("myBoard[].teamName").description("팀의 이름"),
                                fieldWithPath("myBoard[].details[]").description("보드의 상세정보"),
                                fieldWithPath("myBoard[].details[].boardId").description("보드의 아이디"),
                                fieldWithPath("myBoard[].details[].title").description("보드의 타이틀"),
                                fieldWithPath("myBoard[].details[].description").description("보드의 설명")
                        )
                ));
    }

    @Test
    void findBoardById() throws Exception {

        //given
        UserDetails userDetail = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtTokenUtil.generateToken((MemberInfo) userDetail);

        String boardUrl = "/api/board";

        mockMvc.perform(RestDocumentationRequestBuilders.get(boardUrl+"/{boardId}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                    .with(user(userDetail))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andDo(document(
                        "retrieve-detail-board",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증토큰"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                        ),
                        responseFields(
                                fieldWithPath("cardLists").description("해당 보드의 카드 리스트들"),
                                fieldWithPath("cardLists[].id").description("카드리스트의 아이디"),
                                fieldWithPath("cardLists[].name").description("카드리스트의 이름"),
                                fieldWithPath("cardLists[].position").description("카드리스트의 위치"),
                                fieldWithPath("cardLists[].cards").description("해당 카드리스트의 카드들"),
                                fieldWithPath("cardLists[].cards[].id").description("카드의 아이디"),
                                fieldWithPath("cardLists[].cards[].title").description("카드의 타이틀"),
                                fieldWithPath("cardLists[].cards[].description").description("카드의 설명"),
                                fieldWithPath("cardLists[].cards[].position").description("카드의 위치"),
                                fieldWithPath("members").description("해당 보드의 속한 멤버들"),
                                fieldWithPath("members[].memberId").description("멤버의 아이디"),
                                fieldWithPath("members[].username").description("멤버의 닉네임"),
                                fieldWithPath("team").description("보드가 속한 팀"),
                                fieldWithPath("team.name").description("보드가 속한 팀의 이름"),
                                fieldWithPath("board").description("해당 보드"),
                                fieldWithPath("board.description").description("해당 보드의 설명"),
                                fieldWithPath("board.title").description("해당 보드의 타이틀"),
                                fieldWithPath("board.id").description("해당 보드의 아이디")
                        ),
                        pathParameters(
                                parameterWithName("boardId").description("상세보기를 원하는 board의 id")
                        )
                ));

    }
}