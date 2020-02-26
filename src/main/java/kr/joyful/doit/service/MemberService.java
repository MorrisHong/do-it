package kr.joyful.doit.service;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncode;

    public Long join(Member member) {
        member.encodePassword(passwordEncode);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException(username);
        }

        UserDetails user;
        if (username.contains("@")) {
            Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
            return User.builder()
                    .username(member.getEmail())
                    .password(member.getPassword())
                    .roles(member.getRole().name())
                    .build();
        } else {
            Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
            return User.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .roles(member.getRole().name())
                    .build();
        }
    }
}
