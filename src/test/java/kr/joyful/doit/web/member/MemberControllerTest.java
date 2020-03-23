package kr.joyful.doit.web.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.config.RestDocsConfiguration;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.jwt.JwtAuthenticationGenerator;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    JwtAuthenticationGenerator jwtAuthenticationGenerator;

    @Test
    @DisplayName("body에 아무것도 없이 회원가입. 400에러 기대")
    void expected_40x_error() throws Exception {

        String joinUrl = "/api/member";
        mockMvc.perform(post(joinUrl)
                    .servletPath(joinUrl))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    @DisplayName("정상적인 회원가입 201 기대")
    @WithAnonymousUser
    void successful_join() throws Exception {
        String email = "mockMember@email.com";
        String username = "mockMember";
        String password = "123456";
        String password2 = "123456";

        MemberJoinRequestDto memberDto = createMockMemberDto(email, username, password, password2);


        String joinUrl = "/api/member";
        mockMvc.perform(post(joinUrl)
                    .servletPath(joinUrl)
                    .content(objectMapper.writeValueAsString(memberDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document(
                        "create-member",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type Header")
                        ),
                        requestFields(
                                fieldWithPath("email").description("사용자의 이메일주소"),
                                fieldWithPath("username").description("사용자가 사이트 내에서 사용할 이름"),
                                fieldWithPath("password").description("사용자의 비밀번호"),
                                fieldWithPath("password2").description("사용자의 비밀번호 확인")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header")
                        )
                ));
    }

    @Test
    @DisplayName("짧은 비밀번호 회원가입. BadRequest 기대")
    void invalid_password_join() throws Exception {
        String email = "mockMember@email.com";
        String username = "mockMember";
        String password = "1234";
        String password2 = "1234";

        MemberJoinRequestDto memberDto = createMockMemberDto(email, username, password, password2);

        String joinUrl = "/api/member";
        mockMvc.perform(post(joinUrl)
                    .servletPath(joinUrl)
                    .content(objectMapper.writeValueAsString(memberDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("MemberController - findMember. (내 프로필 보기.)")
    void findMember_isOwner_true() throws Exception {
        //given
        String email = "mock@email.com";
        String username = "mockMember";
        String password = "1234";
        MemberRole role = MemberRole.MEMBER;

        Member member = new Member(email, username, password, role);
        Member saveMember = memberRepository.save(member);
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(memberService.loadUserByUsername(member.getEmail()));

        //when, then
        String joinUrl = "/api/member";

        mockMvc.perform(get(joinUrl + "/{memberId}", saveMember.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("username").value(username))
                .andExpect(jsonPath("owner").value(true))

                .andDo(document(
                        "find-member",
                        responseFields(
                                fieldWithPath("email").description("사용자의 이메일주소"),
                                fieldWithPath("username").description("사용자가 사이트 내에서 사용할 이름"),
                                fieldWithPath("owner").description("프로필의 주인이면 true, 아니면 false")
                        )
                ));
    }

    @Test
    @DisplayName("MemberController - findMember. (다른 사용자의 프로필 보기.)")
    void findMember_isOwner_false() throws Exception {
        //given
        String email = "mock@email.com";
        String username = "mockMember";
        String password = "1234";
        MemberRole role = MemberRole.MEMBER;

        Member member = new Member(email, username, password, role);
        memberRepository.save(member);
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(memberService.loadUserByUsername(member.getEmail()));

        //when, then
        String loginEmail = "login@member.com";
        String anotherUsername = "anotherMock";

        Member anotherMember = new Member(loginEmail, anotherUsername, "1234", MemberRole.MEMBER);
        Member loginMember = memberRepository.save(anotherMember);
        String joinUrl = "/api/member";

        mockMvc.perform(get(joinUrl + "/{memberId}", loginMember.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(loginEmail))
                .andExpect(jsonPath("username").value(anotherUsername))
                .andExpect(jsonPath("owner").value(false));
    }

    @Test
    @DisplayName("멤버이름 수정하기 - 올바른 권한")
    void updateMemberName_success() throws Exception {
        //given
        String email = "mock@email.com";
        String username = "mockMember";
        String password = "1234";
        MemberRole role = MemberRole.MEMBER;

        Member member = new Member(email, username, password, role);
        memberRepository.save(member);
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(memberService.loadUserByUsername(member.getEmail()));

        //when, then
        String updateUsername = "updateUsername";
        MemberUpdateNameRequestDto dto = new MemberUpdateNameRequestDto(updateUsername);
        String joinUrl = "/api/member";

        mockMvc.perform(put(joinUrl + "/{memberId}", member.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.createAuthenticationHeaderString())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        Member findMember = memberRepository.findById(member.getId()).get();
        assertEquals(updateUsername, findMember.getUsername());

    }

    @Test
    @DisplayName("password, password2가 서로 다를 때")
    void password_invalid() throws Exception {
        //given
        String email = "mock@email.com";
        String username = "mockMember";
        String password = "123456";
        String password2 = "123455";

        MemberJoinRequestDto mockMemberDto = createMockMemberDto(email, username, password, password2);

        //when, then
        String joinUrl = "/api/member";
        mockMvc.perform(post(joinUrl)
                    .servletPath(joinUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockMemberDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private MemberJoinRequestDto createMockMemberDto(String email, String username, String password, String password2) {
        return MemberJoinRequestDto.builder()
                .email(email)
                .username(username)
                .password(password)
                .password2(password2)
                .build();
    }

}