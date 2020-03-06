package kr.joyful.doit.service.cardList;

public class CardListNotFoundException extends RuntimeException {
    public CardListNotFoundException(Long cardListId) {
        super("해당하는 카드리스트를 찾지 못했습니다. id : " + cardListId);
    }
}
