package kr.joyful.doit.web.team;

import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;
import lombok.Data;
import lombok.Getter;

@Getter
public class TeamCreateRequestDto {
    private String teamName;
    private String description;

    public TeamCreateRequestDto(String teamName, String description) {
        this.teamName = teamName;
        this.description = description;
    }

    public Team toEntity(Member member) {
        return Team.builder()
                .owner(member)
                .description(this.description)
                .name(teamName)
                .build();
    }
}
