package kr.joyful.doit.service.board;

import kr.joyful.doit.domain.boardMember.BoardMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Test
    void basic() throws Exception {
        List<BoardMember> boardMemberByBoardId = boardService.findBoardMemberByBoardId(1L);
        assertEquals(2, boardMemberByBoardId.size());
    }
}