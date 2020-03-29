package kr.joyful.doit.web.card;

import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.service.card.CardService;
import kr.joyful.doit.service.cardList.CardListService;
import kr.joyful.doit.web.result.ApiResult;
import kr.joyful.doit.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class CardController {

    private final CardService cardService;
    private final CardListService cardListService;

    @PostMapping("/api/cards")
    public ResponseEntity<ApiResult> addCard(@RequestBody CardAddRequestDto dto) throws URISyntaxException {
        CardList cardList = cardListService.findById(dto.getCardListId());
        cardService.addCard(dto.toEntity(cardList));
        return Result.created();
    }

    @PutMapping("/api/cards/{cardId}")
    public ResponseEntity<ApiResult> changePosition(@PathVariable Long cardId, @RequestBody Integer newPosition) {
        cardService.changeCardPosition(cardId, newPosition);
        return Result.ok();
    }
}
