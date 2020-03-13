package kr.joyful.doit.service.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.board.BoardRepository;
import kr.joyful.doit.domain.boardMember.BoardMember;
import kr.joyful.doit.domain.boardMember.BoardMemberRepository;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    public Long save(Board board, Member member) {
        Optional<Board> boardByTitleAndTeam = boardRepository.findBoardByTitleAndTeam(board.getTitle(), board.getTeam());
        if (boardByTitleAndTeam.isPresent()) {
            throw new BoardAlreadyExsistException();
        }
        BoardMember boardMember = BoardMember.create(board, member);
        boardMemberRepository.save(boardMember);
        boardRepository.save(board);
        return board.getId();
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException(boardId));
    }

    public List<Board> findMyBoardList(Member member) {
        return boardRepository.findMyBoardList(member).orElse(Collections.emptyList());
    }

    public List<BoardMember> findBoardMemberByBoardId(Long boardId) {
        return boardMemberRepository.findBoardMemberById(boardId);
    }

    public void invite(Board board, Member member) {
        BoardMember boardMember = BoardMember.create(board, member);
        boardMemberRepository.save(boardMember);
    }

    public List<Member> findBoardMembersByBoardId(Long boardId) {
        return boardRepository.findBoardMembersByBoardId(boardId);
    }
}
