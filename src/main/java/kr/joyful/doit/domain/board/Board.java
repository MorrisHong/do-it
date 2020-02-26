package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.common.BaseEntity;
import kr.joyful.doit.domain.team.Team;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private String title;
    private String description;

    private Board(Team team, String title, String description) {
        this.team = team;
        this.title = title;
        this.description = description;
    }

    public static Board create(Team team, String title, String description) {
        return new Board(team, title, description);
    }
}
