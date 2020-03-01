package kr.joyful.doit.web.member.dto;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MemberValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MemberJoinRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberJoinRequestDto dto = (MemberJoinRequestDto) target;
        if (!dto.getPassword().equals(dto.getPassword2())) {
            errors.reject("wrongPassword");
        }
    }
}
