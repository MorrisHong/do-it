package kr.joyful.doit.service.card;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.card.CardRepository;
import kr.joyful.doit.domain.card.CardStatus;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.cardList.CardListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    CardService cardService;
    CardRepository cardRepository;
    CardListRepository cardListRepository;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        cardListRepository = mock(CardListRepository.class);
        cardService = new CardService(cardRepository, cardListRepository);
    }

    @Test
    void create() {
        Card card = Card.create("testCard", "test card", CardStatus.ACTIVATE, null, 0);

        cardService.addCard(card);

        verify(cardRepository).save(card);
    }

    @Test
    void findCard() {
        Board boardA = Board.create(null, "testBoardA", "this is boardA");
        Board boardB = Board.create(null, "testBoardB", "this is boardB");

        CardList cardList1 = CardList.createCardList(boardA, "cardListA", 0);
        CardList cardList2 = CardList.createCardList(boardB, "cardListB", 2);

        Card card = Card.create("testCard1", "test card", CardStatus.ACTIVATE, cardList1, 0);
        Card card2 = Card.create("testCard2", "test card", CardStatus.ACTIVATE, cardList1, 1);
        Card card3 = Card.create("testCard3", "test card", CardStatus.ACTIVATE, cardList2, 2);

        cardService.addCard(card);
        cardService.addCard(card2);
        cardService.addCard(card3);

        given(cardRepository.findCardsByBoard(any())).willReturn(List.of(card, card2));
        List<Card> cardList = cardService.findByBoardId(boardA);
        assertEquals(2, cardList.size());
    }

    @Test
    void changeCardPosition() {
        Card card = Card.create("testCard1", "test card", CardStatus.ACTIVATE, null, 0);

        cardService.addCard(card);

        given(cardRepository.findByCardId(any())).willReturn(Optional.of(card));
        int newPosition = cardService.changeCardPosition(0L, 2);

        assertEquals(2, newPosition);
    }

    @Test
    void changeCardList() {
        CardList cardList = CardList.createCardList(null, "cardListA", 0);
        CardList newCardList = CardList.createCardList(null, "newCardList", 0);

        Card card = Card.create("testCard", "test card", CardStatus.ACTIVATE, cardList, 0);

        given(cardListRepository.findById(1L)).willReturn(Optional.of(newCardList));
        given(cardRepository.findByCardId(any())).willReturn(Optional.of(card));

        cardService.changeCardList(1L, 0L, 2);
        assertEquals(newCardList.getName(), card.getCardList().getName());
    }

}