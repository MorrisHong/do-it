package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findBoardByTitleAndTeam(String title, Team team);

    @Query("select b from Board as b" +
            " left join fetch b.team bt" +
            " left join fetch bt.owner bto" +
            " where b.id = :boardId")
    Optional<Board> findById(Long boardId);
}
