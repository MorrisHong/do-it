package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.service.board.BoardService;
import kr.joyful.doit.service.card.CardService;
import kr.joyful.doit.service.cardList.CardListService;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.service.team.TeamService;
import kr.joyful.doit.web.member.CurrentUser;
import kr.joyful.doit.web.member.MemberInfo;
import kr.joyful.doit.web.result.ApiResult;
import kr.joyful.doit.web.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final CardListService cardListService;
    private final CardService cardService;

    @PostMapping("/api/boards")
    public ResponseEntity<ApiResult> create(@RequestBody BoardCreateRequestDto dto, @CurrentUser MemberInfo memberInfo) throws URISyntaxException {
        Team findTeam = teamService.findTeamById(dto.getTeamId());
        boardService.save(dto.toEntity(findTeam), memberInfo.getMember());
        return Result.created();
    }

    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<ApiResult> findBoardById(@PathVariable Long boardId) {
        Board findBoard = boardService.findById(boardId);
        List<Member> findBoardMember = boardService.findBoardMembersByBoardId(boardId);
        Team findTeam = teamService.findTeamById(findBoard.getTeam().getId());
        List<CardList> findCardList = cardListService.findByBoardId(boardId);
        List<Card> findCards = cardService.findByBoardId(findBoard);

        return BoardResult.build(findTeam, findBoard, findBoardMember, findCardList, findCards);
    }

    @GetMapping("/api/boards")
    public ResponseEntity<ApiResult> findMyBoardList(@CurrentUser MemberInfo memberInfo) {
        List<Board> boards = boardService.findMyBoardList(memberInfo.getMember());
        return MyBoardResult.build(boards);
    }

    @PutMapping("/api/boards/{boardId}/members/{memberId}")
    public ResponseEntity<ApiResult> inviteMemberInBoard(@PathVariable Long boardId, @PathVariable Long memberId) {
        Board findBoard = boardService.findById(boardId);
        Member findMember = memberService.findMemberById(memberId);
        boardService.invite(findBoard, findMember);
        return Result.ok();
    }
}
