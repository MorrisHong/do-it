package kr.joyful.doit.model.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MemberRepositoryTest {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findByEmail() {
        String email = "mockMember@Email.com";
        String username = "mockMember";
        String password = "123456";

        Member member = Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByEmail(email).get();
        assertEquals(findMember.getEmail(), member.getEmail());
        assertEquals(findMember.getCreatedDate(), member.getCreatedDate());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember.getId(), member.getId());

        assertEquals(findMember, member);
    }


    @Test
    void findByUsername() {
        String email = "mockMember@Email.com";
        String username = "mockMember";
        String password = "123456";

        Member member = Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        memberRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByUsername(username).get();
        assertEquals(findMember.getEmail(), member.getEmail());
        assertEquals(findMember.getCreatedDate(), member.getCreatedDate());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember.getId(), member.getId());

        assertEquals(findMember, member);
    }
}