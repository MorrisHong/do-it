package kr.joyful.doit.domain.member;

import kr.joyful.doit.domain.common.BaseTimeEntity;
import kr.joyful.doit.domain.team.Team;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;


    @Builder
    public Member(String email, String username, String password, MemberRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void encodePassword(PasswordEncoder passwordEncode) {
        this.password = passwordEncode.encode(password);
    }

    public static Member create(String email, String username, String password, MemberRole role) {
        return new Member(email, username, password, role);
    }
}
