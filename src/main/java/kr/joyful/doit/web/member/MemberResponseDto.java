package kr.joyful.doit.web.member;

import kr.joyful.doit.domain.member.Member;
import lombok.Data;

@Data
class MemberResponseDto {

    private String email;
    private String username;
    private boolean isOwner;

    public MemberResponseDto(Member member) {
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.isOwner = false;
    }
}
