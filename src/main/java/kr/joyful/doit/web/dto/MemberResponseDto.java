package kr.joyful.doit.web.dto;

import kr.joyful.doit.domain.member.Member;
import lombok.Data;

@Data
public class MemberResponseDto {

    private String email;
    private String username;

    public MemberResponseDto(Member member) {
        this.email = member.getEmail();
        this.username = member.getUsername();
    }
}
