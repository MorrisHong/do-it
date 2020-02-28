package kr.joyful.doit.domain.card;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Card extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    private String title;
    private String description;
    private CardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_list_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CardList cardList;

    private Card(String title, String description, CardStatus status, CardList cardList) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.cardList = cardList;
    }

    public static Card create(String title, String description, CardStatus status, CardList cardList) {
        return new Card(title, description, status, cardList);
    }
}
