package kr.joyful.doit.domain.card;

import kr.joyful.doit.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface CardRepositoryCustom {
    Optional<Card> findByCardId(Long cardId);
    List<Card> findCardsByBoard(Board board);
}
