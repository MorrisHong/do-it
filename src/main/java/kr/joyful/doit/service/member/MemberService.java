package kr.joyful.doit.service.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncode;

    public Long join(Member member) {
        ifExistsMemberThrowException(member);
        member.encodePassword(passwordEncode);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(usernameOrEmail)) {
            throw new UsernameNotFoundException(usernameOrEmail);
        }

        if (usernameOrEmail.contains("@")) {
            Member member = memberRepository.findByEmail(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));
            return User.builder()
                    .username(member.getEmail())
                    .password(member.getPassword())
                    .roles(member.getRole().name())
                    .build();
        } else {
            Member member = memberRepository.findByUsername(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException(usernameOrEmail));
            return User.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .roles(member.getRole().name())
                    .build();
        }
    }

    private void ifExistsMemberThrowException(Member member) {
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        Optional<Member> byUsername = memberRepository.findByUsername(member.getUsername());
        if (byEmail.isPresent() || byUsername.isPresent()) {
            throw new MemberAlreadyExistsException();
        }
    }
}
