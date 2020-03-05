package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.boardMember.BoardMember;
import kr.joyful.doit.domain.common.BaseEntity;
import kr.joyful.doit.domain.team.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Team team;

    private String title;
    private String description;

    private int position;

    @Builder
    private Board(Team team, String title, String description, int position) {
        this.team = team;
        this.title = title;
        this.description = description;
        this.position = position;
    }

    public static Board create(Team team, String title, String description, int position) {
        return new Board(team, title, description, position);
    }

}
