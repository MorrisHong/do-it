package kr.joyful.doit.service.team;

import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public Long createTeam(Team team) {
        teamRepository.save(team);
        return team.getId();
    }
}
