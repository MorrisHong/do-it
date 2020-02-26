package kr.joyful.doit.web.dto;

import kr.joyful.doit.domain.member.Member;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberJoinRequestDto {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String username;

    @NotNull
    @Size(min = 6, max = 30, message = "비밀번호는 6글자에서 30글자까지 허용됩니다.")
    private String password;

    @NotNull
    private String password2;

    @Builder
    public MemberJoinRequestDto(@NotEmpty @Email String email, @NotEmpty String username, @NotNull @Size(min = 6, max = 30, message = "비밀번호는 6글자에서 30글자까지 허용됩니다.") String password, @NotNull String password2) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();
    }
}
