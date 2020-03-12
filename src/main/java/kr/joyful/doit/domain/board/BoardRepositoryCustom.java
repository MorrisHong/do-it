package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.team.Team;

import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findBoardByTitleAndTeam(String title, Team team);
}
