package kr.joyful.doit.web.member;

import kr.joyful.doit.domain.member.Member;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.EntityListeners;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
public class MemberInfo extends User {

    private Member member;
    private String email;

    public MemberInfo(Member member) {
        super(member.getUsername(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())));
        this.member = member;
        this.email = member.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public Member getMember() {
        return member;
    }
}
