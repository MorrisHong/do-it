package kr.joyful.doit.web.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.joyful.doit.config.RestDocsConfiguration;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.card.CardRepository;
import kr.joyful.doit.domain.card.CardStatus;
import kr.joyful.doit.jwt.JwtAuthenticationGenerator;
import kr.joyful.doit.jwt.JwtTokenType;
import kr.joyful.doit.jwt.JwtTokenUtil;
import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.web.cardList.CardListAddRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(RestDocsConfiguration.class)
@AutoConfigureRestDocs
class CardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserDetailsService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtAuthenticationGenerator jwtAuthenticationGenerator;

    @Autowired
    CardRepository cardRepository;


    @Test
    void create() throws Exception {

        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);
        CardAddRequestDto dto = CardAddRequestDto.builder()
                .cardListId(1L)
                .cardStatus(CardStatus.ACTIVATE)
                .description("this is card A")
                .title("cardA")
                .build();

        mockMvc.perform(post("/api/cards")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.getAccessToken())

                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())

                .andDo(document(
                        "create-card",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                        ),
                        requestFields(
                                fieldWithPath("title").description("카드의 제목"),
                                fieldWithPath("description").description("카드의 설명"),
                                fieldWithPath("cardStatus").description("카드의 상태 기본은 'ACTIVATE'"),
                                fieldWithPath("cardListId").description("카드가 속한 카드리스트의 id")
                        )
                ));
    }

    @Test
    void updatePosition() throws Exception {
        Card card = Card.create("test card", "description", CardStatus.ACTIVATE, null, 0);
        Card saveCard = cardRepository.save(card);

        UserDetails userDetails = memberService.loadUserByUsername("member1@example.com");
        JwtAuthenticationDto jwtAuthenticationDto = jwtAuthenticationGenerator.createJwtAuthenticationFromUserDetails(userDetails);


        mockMvc.perform(put("/api/cards/{cardId}", saveCard.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuthenticationDto.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("2"))
                .andDo(print())
                .andExpect(status().isOk());

        Card findCard = cardRepository.findByCardId(saveCard.getId()).get();
        assertEquals(2, findCard.getPosition());
    }

}