package kr.joyful.doit.domain.cardList;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
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
}
