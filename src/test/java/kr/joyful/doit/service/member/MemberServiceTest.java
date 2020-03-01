package kr.joyful.doit.service.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberService memberService;

    @Test
    void join() {
        Member member = Member.builder()
                .email("mock@email.com")
                .username("mockMember")
                .password("1234")
                .build();

        given(memberService.join(member)).willReturn(1L);

        Long memberId = memberService.join(member);

        given(memberService.findMember(memberId)).willReturn(member);

        Member findMember = memberService.findMember(memberId);

        assertEquals(findMember.getEmail(), member.getEmail());
    }


}