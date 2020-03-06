package kr.joyful.doit.web.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.domain.card.CardStatus;
import kr.joyful.doit.web.cardList.CardListAddRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserDetailsService memberService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void basic() throws Exception {

        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");

        CardAddRequestDto dto = CardAddRequestDto.builder()
                .cardListId(1L)
                .cardStatus(CardStatus.ACTIVATE)
                .description("this is card A")
                .title("cardA")
                .build();

        mockMvc.perform(post("/api/card")
                    .with(user(userDetails))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}