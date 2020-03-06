package kr.joyful.doit.web.card;

import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.service.card.CardService;
import kr.joyful.doit.service.cardList.CardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class CardController {

    private final CardService cardService;
    private final CardListService cardListService;

    @PostMapping("/api/card")
    public ResponseEntity<?> addCard(@RequestBody CardAddRequestDto dto) throws URISyntaxException {
        CardList cardList = cardListService.findById(dto.getCardListId());
        Long cardId = cardService.addCard(dto.toEntity(cardList));
        return ResponseEntity.created(new URI("/api/card/" + cardId)).build();
    }
}
