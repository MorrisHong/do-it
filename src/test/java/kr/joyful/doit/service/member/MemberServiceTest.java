package kr.joyful.doit.service.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    MemberService memberService;
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    @DisplayName("패스워드 인코딩 잘됨")
    void successFul_join() {
        Member member = Member.builder()
                .email("mock@email.com")
                .username("mockMember")
                .password("1234")
                .build();

        when(memberRepository.save(member)).thenReturn(member);
        when(passwordEncoder.encode(member.getPassword())).thenReturn("!@#");

        memberService.join(member);
        assertEquals("!@#", member.getPassword());
    }

    @Test
    @DisplayName("이미 있는 username으로 회원가입시 MemberAlreadyExistsException 터짐")
    void alreadyExistsUsername_memberJoin() {
        String username = "mockMember";
        Member member = Member.builder()
                .email("mock@email.com")
                .username(username)
                .password("1234")
                .build();

        given(memberRepository.findByUsername(username)).willReturn(Optional.of(member));

        assertThrows(MemberAlreadyExistsException.class, () -> memberService.join(member));
    }

    @Test
    @DisplayName("이미 있는 email로 회원가입시 MemberAlreadyExistsException 터짐")
    void alreadyExistsEmail_memberJoin() {
        String email = "mock@email.com";
        Member member = Member.builder()
                .email(email)
                .username("mockMember")
                .password("1234")
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        assertThrows(MemberAlreadyExistsException.class, () -> memberService.join(member));
    }


}