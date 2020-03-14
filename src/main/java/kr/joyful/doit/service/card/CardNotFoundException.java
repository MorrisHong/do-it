package kr.joyful.doit.service.card;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long cardId) {
        super("존재하지 않는 카드입니다. id : " + cardId);
    }
}
