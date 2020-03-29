package kr.joyful.doit.web.cardList;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.service.board.BoardService;
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
public class CardListController {

    private final CardListService cardListService;
    private final BoardService boardService;

    @PostMapping("/api/card-lists")
    public ResponseEntity<ApiResult> addCardList(@RequestBody CardListAddRequestDto dto) throws URISyntaxException {
        Long boardId = dto.getBoardId();
        Board findBoard = boardService.findById(boardId);
        cardListService.addCardList(dto.toEntity(findBoard));
        return Result.created();
    }

    //todo : ENTITY 직접반환하지 말고 dto로 바꿔야함. (CardListResponseDto.class)
    @GetMapping("/api/card-list/{cardListId}")
    public ResponseEntity<ApiResult> getCardListById(@PathVariable Long cardListId) {
        CardList cardList = cardListService.findById(cardListId);

        return null;
    }
}
