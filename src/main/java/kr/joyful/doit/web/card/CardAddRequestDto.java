package kr.joyful.doit.web.card;

import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.card.CardStatus;
import kr.joyful.doit.domain.cardList.CardList;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CardAddRequestDto {

    private String title;
    private String description;
    private CardStatus cardStatus;
    private Long CardListId;

    @Builder
    public CardAddRequestDto(String title, String description, CardStatus cardStatus, Long cardListId) {
        this.title = title;
        this.description = description;
        this.cardStatus = cardStatus;
        this.CardListId = cardListId;
    }

    public Card toEntity(CardList cardList) {
        return Card.create(title, description, cardStatus, cardList, 0);
    }
}
