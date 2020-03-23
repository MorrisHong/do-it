package kr.joyful.doit.web.member;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class MemberUpdateNameRequestDto {

    @NotEmpty
    private String username;

    public MemberUpdateNameRequestDto() {
    }

    public MemberUpdateNameRequestDto(String username) {
        this.username = username;
    }
}
