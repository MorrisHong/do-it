package kr.joyful.doit.jwt;

import kr.joyful.doit.jwt.dto.JwtAuthenticationDto;
import kr.joyful.doit.web.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationGenerator {

    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationDto createJwtAuthenticationFromUserDetails(UserDetails userDetails) {
        MemberInfo memberInfo = (MemberInfo) userDetails;
        return JwtAuthenticationDto.createAuthenticationFromTokens(
                jwtTokenUtil.generateToken(memberInfo.getEmail(), JwtTokenType.AUTH),
                jwtTokenUtil.generateToken(memberInfo.getEmail(), JwtTokenType.REFRESH)
        );
    }
}
