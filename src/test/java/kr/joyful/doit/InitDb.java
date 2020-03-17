package kr.joyful.doit;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.card.CardStatus;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.service.board.BoardService;
import kr.joyful.doit.service.card.CardService;
import kr.joyful.doit.service.cardList.CardListService;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.service.team.TeamService;
import kr.joyful.doit.web.cardList.CardListController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@RequiredArgsConstructor
@ActiveProfiles("test")
public class InitDb implements ApplicationRunner {

    private final MemberService memberService;
    private final TeamService teamService;
    private final BoardService boardService;
    private final CardListService cardListService;
    private final CardService cardService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member1 = Member.builder()
                .username("member1")
                .email("member1@example.com")
                .password("1234")
                .role(MemberRole.MEMBER)
                .build();
        memberService.join(member1);

        Member member2 = Member.builder()
                .username("member")
                .email("member2@example.com")
                .password("1234")
                .role(MemberRole.MEMBER)
                .build();
        memberService.join(member2);

        Team team1 = Team.create("teamA", "this is team A", member1, 0);
        teamService.createTeam(team1);
        Team team2 = Team.create("teamB", "this is team B", member1, 0);
        teamService.createTeam(team2);

        Board board1 = Board.create(team1, "BoardA", "this is board A");
        boardService.save(board1, member1);
        boardService.invite(board1, member2);

        Board board2 = Board.create(team1, "BoardB", "this is board B");
        boardService.save(board2, member1);

        Board board3 = Board.create(team2, "BoardC", "this is board C");
        boardService.save(board3, member1);

        Board board4 = Board.create(team2, "BoardD", "this is board D");
        boardService.save(board4, member1);

        CardList cardList1 = CardList.builder()
                .board(board1)
                .name("testCardList")
                .position(0)
                .build();

        cardListService.addCardList(cardList1);

        Card cardA = Card.create("testCardA", "test card A", CardStatus.ACTIVATE, cardList1, 0);
        Card cardB = Card.create("testCardB", "test card B", CardStatus.ACTIVATE, cardList1, 1);
        cardService.addCard(cardA);
        cardService.addCard(cardB);


    }
}
