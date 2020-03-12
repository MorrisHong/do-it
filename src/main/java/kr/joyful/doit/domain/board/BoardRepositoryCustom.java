package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findBoardByTitleAndTeam(String title, Team team);

    Optional<List<Board>> findMyBoardList(Member member);
}
