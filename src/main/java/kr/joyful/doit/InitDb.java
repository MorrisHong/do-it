package kr.joyful.doit;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.service.board.BoardService;
import kr.joyful.doit.service.member.MemberService;
import kr.joyful.doit.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb implements ApplicationRunner {

    private final MemberService memberService;
    private final TeamService teamService;
    private final BoardService boardService;

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

        Team team = Team.create("teamA", "this is team A", member1, 0);
        teamService.createTeam(team);

        Board board1 = Board.create(team, "BoardA", "this is board A");
        boardService.save(board1, member1);
        boardService.invite(board1, member2);


        Board board2 = Board.create(team, "BoardB", "this is board B");
        boardService.save(board2, member1);

    }
}
