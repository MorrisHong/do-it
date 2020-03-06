package kr.joyful.doit.web.cardList;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.cardList.CardList;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CardListAddRequestDto {

    private String name;
    private int position;
    private Long boardId;


    public CardList toEntity(Board board) {
        return CardList.builder()
                .board(board)
                .name(name)
                .position(position)
                .build();
    }

    @Builder
    public CardListAddRequestDto(String name, int position, Long boardId) {
        this.name = name;
        this.position = position;
        this.boardId = boardId;
    }
}
