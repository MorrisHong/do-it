package kr.joyful.doit.service.card;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.card.CardRepository;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.cardList.CardListRepository;
import kr.joyful.doit.service.cardList.CardListNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    public Long addCard(Card card) {
        cardRepository.save(card);
        return card.getId();
    }

    public List<Card> findByBoardId(Board board) {
        return cardRepository.findCardsByBoard(board);
    }

    public int changeCardPosition(Long cardId, int newPosition) {
        Card card = cardRepository.findByCardId(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        return card.changeCardPosition(newPosition);
    }

    public Long changeCardList(Long newCardListId, Long cardId, int newPosition) {
        Card card = cardRepository.findByCardId(cardId).orElseThrow(() -> new CardNotFoundException(cardId));
        CardList newCardList = cardListRepository.findById(newCardListId).orElseThrow(() -> new CardListNotFoundException(newCardListId));
        return card.changeCardList(newCardList, newPosition);
    }
}
