package kr.joyful.doit;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.service.member.MemberService;
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


    }
}
