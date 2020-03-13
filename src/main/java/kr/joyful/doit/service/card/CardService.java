package kr.joyful.doit.service.card;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.card.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;

    public Long addCard(Card card) {
        cardRepository.save(card);
        return card.getId();
    }

    public List<Card> findByBoardId(Board board) {
        return cardRepository.findCardsByBoard(board);
    }
}
