package kr.joyful.doit.service.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberJoinManagement {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(String username, String email, String password) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            throw new MemberAlreadyExistsException();
        }

        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            throw new MemberAlreadyExistsException();
        }

        Member member = Member.create(email, username, password, MemberRole.MEMBER);
        memberRepository.save(member);
        return member;
    }
}
