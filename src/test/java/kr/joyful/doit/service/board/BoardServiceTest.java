package kr.joyful.doit.service.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.board.BoardRepository;
import kr.joyful.doit.domain.boardMember.BoardMember;
import kr.joyful.doit.domain.boardMember.BoardMemberRepository;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BoardServiceTest {

    BoardService boardService;
    BoardMemberRepository boardMemberRepository;
    BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        boardMemberRepository = mock(BoardMemberRepository.class);
        boardRepository = mock(BoardRepository.class);
        boardService = new BoardService(boardRepository, boardMemberRepository);
    }

    @Test
    void findById() throws Exception {
        //given
        String title = "boardA";
        String desc = "this is boardA";
        Optional<Board> board = Optional.ofNullable(Board.create(null, title, desc));
        given(boardRepository.findById(1L)).willReturn(board);

        //when
        Board findBoard = boardService.findById(1L);

        //then
        assertEquals(title, findBoard.getTitle());
        assertEquals(desc, findBoard.getDescription());
    }

    @Test
    void findBoardMemberByBoardId() {
        //given
        Board board = Board.create(null, "boardA", "this is boardA");
        Member member = Member.create("test@email.com", "testMember", "1234", MemberRole.MEMBER);
        BoardMember boardMember = BoardMember.create(board, member);
        boardService.save(board, member);

        //when
        when(boardService.findBoardMemberByBoardId(1L)).thenReturn(List.of(boardMember));
        List<BoardMember> boardMemberByBoardId = boardService.findBoardMemberByBoardId(1L);

        //then
        Member member1 = boardMemberByBoardId.get(0).getMember();
        assertEquals(member1.getEmail(), member.getEmail());
        assertEquals(member1.getUsername(), member.getUsername());
    }

}