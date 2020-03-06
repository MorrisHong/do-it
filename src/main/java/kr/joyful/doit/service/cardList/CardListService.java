package kr.joyful.doit.service.cardList;

import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.cardList.CardListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CardListService {

    private final CardListRepository cardListRepository;

    public Long addCardList(CardList cardList) {
        cardListRepository.save(cardList);
        return cardList.getId();
    }

    public CardList findById(Long cardListId) {
        return cardListRepository.findById(cardListId).orElseThrow( () -> new CardListNotFoundException(cardListId));
    }
}
