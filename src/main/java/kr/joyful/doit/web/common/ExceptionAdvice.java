package kr.joyful.doit.web.common;

import kr.joyful.doit.service.team.TeamAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(TeamAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userNotFoundException(TeamAlreadyExistsException e) {
        return e.getMessage();
    }

}
