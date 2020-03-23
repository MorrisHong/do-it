package kr.joyful.doit.service.member;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.web.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(email)) {
            throw new UsernameNotFoundException(email);
        }

        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));

        return new MemberInfo(findMember);
    }

    private void ifExistsMemberThrowException(Member member) {
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        Optional<Member> byUsername = memberRepository.findByUsername(member.getUsername());
        if (byEmail.isPresent() || byUsername.isPresent()) {
            throw new MemberAlreadyExistsException();
        }
    }

    public Long updateUsername(Long memberId, String username) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        member.updateUsername(username);

        return member.getId();
    }
}
