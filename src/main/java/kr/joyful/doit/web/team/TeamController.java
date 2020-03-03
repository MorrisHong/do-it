package kr.joyful.doit.web.team;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.service.team.TeamService;
import kr.joyful.doit.web.member.CurrentUser;
import kr.joyful.doit.web.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class TeamController {
    private final TeamService teamService;

    @PostMapping("/api/team")
    public ResponseEntity<?> createTeam(@RequestBody TeamCreateRequestDto dto, @CurrentUser MemberInfo memberInfo) throws URISyntaxException {
        Team team = dto.toEntity(memberInfo.getMember());
        Long teamId = teamService.createTeam(team);
        URI url = new URI("api/member/"+teamId);
        return ResponseEntity.created(url).build();
    }
}
