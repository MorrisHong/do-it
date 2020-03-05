package kr.joyful.doit.service.team;

import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public Long createTeam(Team team) {
        Optional<Team> teamByName = teamRepository.findTeamByNameAndOwner(team.getName(), team.getOwner());
        if (teamByName.isPresent()) {
            throw new TeamAlreadyExistsException(team.getName());
        }
        teamRepository.save(team);
        return team.getId();
    }

    public Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(teamId));
    }
}
