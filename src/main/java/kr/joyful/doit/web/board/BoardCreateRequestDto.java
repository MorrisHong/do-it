package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.team.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {
    private String title;
    private String description;
    private Long teamId;

    @Builder
    public BoardCreateRequestDto(Long teamId, String title, String description) {
        this.teamId = teamId;
        this.title = title;
        this.description = description;
    }

    public Board toEntity(Team findTeam) {
        return Board.builder()
                .title(title)
                .description(description)
                .team(findTeam)
                .build();
    }
}
