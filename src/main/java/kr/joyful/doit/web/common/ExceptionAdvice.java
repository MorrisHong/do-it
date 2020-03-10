package kr.joyful.doit.web.common;

import kr.joyful.doit.service.member.MemberNotFoundException;
import kr.joyful.doit.service.team.TeamAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userNotFoundException(MemberNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String teamAlreadyExistsException(TeamAlreadyExistsException e) {
        return e.getMessage();
    }


}
