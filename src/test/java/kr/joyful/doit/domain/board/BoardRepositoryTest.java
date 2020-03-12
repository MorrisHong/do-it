package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void findBoardByTitleAndTeam() {
        //given
        Member owner = Member.builder()
                .email("test@Email.com")
                .role(MemberRole.MEMBER)
                .password("123456")
                .username("testMember")
                .build();
        memberRepository.save(owner);

        String teamName = "testTeam";
        String teamDescription = "test team";
        Team team = Team.create(teamName, teamDescription, owner, 0);
        teamRepository.save(team);

        String title = "testBoard";
        String description = "test board";
        Board board = Board.create(team, title, description);

        boardRepository.save(board);

        em.flush();
        em.clear();
        //when
        Board findBoard = boardRepository.findBoardByTitleAndTeam(title, team).get();

        //then
        assertNotNull(findBoard);
        assertEquals(title, findBoard.getTitle());
        assertEquals(team, findBoard.getTeam());
        assertEquals(teamName, findBoard.getTeam().getName());
        assertEquals(teamDescription, findBoard.getTeam().getDescription());
        assertEquals(description, findBoard.getDescription());
    }
}