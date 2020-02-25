package kr.joyful.doit.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequestDto {

    private String email;
    private String username;
    private String password;
    private String password2;
}
