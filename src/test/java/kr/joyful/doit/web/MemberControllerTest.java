package kr.joyful.doit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.web.dto.MemberJoinRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("body에 아무것도 없이 회원가입. 400에러 기대")
    void expected_40x_error() throws Exception {

        mockMvc.perform(post("/api/member"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @Test
    @DisplayName("정상적인 회원가입 201 기대")
    void successful_join() throws Exception {
        String email = "mockMember@email.com";
        String username = "mockMember";
        String password = "123456";
        String password2 = "123456";

        MemberJoinRequestDto memberDto = createMockMemberDto(email, username, password, password2);


        mockMvc.perform(post("/api/member")
                    .content(objectMapper.writeValueAsString(memberDto))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("짧은 비밀번호 회원가입. BadRequest 기대")
    void invalid_password_join() throws Exception {
        String email = "mockMember@email.com";
        String username = "mockMember";
        String password = "1234";
        String password2 = "1234";

        MemberJoinRequestDto memberDto = createMockMemberDto(email, username, password, password2);

        mockMvc.perform(post("/api/member")
                .content(objectMapper.writeValueAsString(memberDto))
                .contentType(MediaType.APPLICATION_JSON))
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