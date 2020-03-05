package kr.joyful.doit.config;

import kr.joyful.doit.web.member.MemberInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<Object> o = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal);

        if (o.isEmpty()) {
            return Optional.empty();
        } else if (o.get().equals("anonymous") || o.get().equals("anonymousUser")) {
            return Optional.of("anonymous");
        } else {
            return o.map(MemberInfo.class::cast)
                    .map(MemberInfo::getUsername);
        }
    }
}