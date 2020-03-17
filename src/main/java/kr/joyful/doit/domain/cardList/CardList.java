package kr.joyful.doit.domain.cardList;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class CardList extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_list_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Board board;

    private String name;

    private int position;

    @Builder
    private CardList(Board board, String name, int position) {
        this.board = board;
        this.name = name;
        this.position = position;
    }

    public static CardList createCardList(Board board, String name, int position) {
        return new CardList(board, name, position);
    }
}
