package kr.joyful.doit.domain.board;

import kr.joyful.doit.domain.boardMember.BoardMember;
import kr.joyful.doit.domain.boardMember.BoardMemberRepository;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardMemberRepository boardMemberRepository;

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

    @Test
    void findMyBoardList() {
        //given
        Member member1 = Member.builder()
                .email("test@Email.com")
                .role(MemberRole.MEMBER)
                .password("123456")
                .username("testMember")
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .email("test2@Email.com")
                .role(MemberRole.MEMBER)
                .password("123456")
                .username("testMember2")
                .build();
        memberRepository.save(member2);

        String teamName = "testTeam";
        String teamDescription = "test team";
        Team team1 = Team.create(teamName, teamDescription, member1, 0);
        teamRepository.save(team1);

        String teamName1 = "testTeam2";
        String teamDescription2 = "test team2";
        Team team2 = Team.create(teamName1, teamDescription2, member2, 0);
        teamRepository.save(team2);

        String title = "testBoard";
        String description = "test board";
        Board board1 = Board.create(team1, title, description);
        boardRepository.save(board1);

        String title2 = "testBoard";
        String description2 = "test board";
        Board board2 = Board.create(team1, title2, description2);
        boardRepository.save(board2);

        String title3 = "testBoard";
        String description3 = "test board";
        Board board3 = Board.create(team2, title3, description3);
        boardRepository.save(board3);

        BoardMember boardMember1 = BoardMember.create(board1, member1);
        BoardMember boardMember2 = BoardMember.create(board2, member1);
        BoardMember boardMember3 = BoardMember.create(board3, member2);

        boardMemberRepository.save(boardMember1);
        boardMemberRepository.save(boardMember2);
        boardMemberRepository.save(boardMember3);

        em.flush();
        em.clear();

        //when

        List<Board> boards = boardRepository.findMyBoardList(member1).get();
        assertEquals(2, boards.size());


        //then
    }

}