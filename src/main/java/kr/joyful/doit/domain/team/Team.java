package kr.joyful.doit.domain.team;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.common.BaseEntity;
import kr.joyful.doit.domain.common.BaseTimeEntity;
import kr.joyful.doit.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member owner;

    private int position;

    private Team(String name, String description, Member owner, int position) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.position = position;
    }

    public static Team create(String name, String description, Member owner, int position) {
        return new Team(name, description, owner, position);
    }
}
