package kr.joyful.doit.web.cardList;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.service.board.BoardService;
import kr.joyful.doit.service.cardList.CardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class CardListController {

    private final CardListService cardListService;
    private final BoardService boardService;

    @PostMapping("/api/card-list")
    public ResponseEntity<?> addCardList(@RequestBody CardListAddRequestDto dto) throws URISyntaxException {
        Long boardId = dto.getBoardId();
        Board findBoard = boardService.findById(boardId);
        Long cardListId = cardListService.addCardList(dto.toEntity(findBoard));
        return ResponseEntity.created(new URI("/api/card-list/"+cardListId)).build();
    }

    //todo : ENTITY 직접반환하지 말고 dto로 바꿔야함. (CardListResponseDto.class)
    @GetMapping("/api/card-list/{cardListId}")
    public ResponseEntity<?> getCardListById(@PathVariable Long cardListId) {
        return ResponseEntity.ok(cardListService.findById(cardListId));
    }
}
