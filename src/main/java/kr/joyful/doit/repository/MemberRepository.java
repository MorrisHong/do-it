package kr.joyful.doit.repository;

import kr.joyful.doit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
