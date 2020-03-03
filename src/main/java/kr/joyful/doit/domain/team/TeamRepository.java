package kr.joyful.doit.domain.team;

import kr.joyful.doit.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findTeamByNameAndOwner(String name, Member member);
}
