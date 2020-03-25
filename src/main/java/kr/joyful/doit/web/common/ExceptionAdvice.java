package kr.joyful.doit.web.common;

import kr.joyful.doit.service.board.BoardAlreadyExistsException;
import kr.joyful.doit.service.board.BoardNotFoundException;
import kr.joyful.doit.service.card.CardNotFoundException;
import kr.joyful.doit.service.cardList.CardListNotFoundException;
import kr.joyful.doit.service.member.MemberAlreadyExistsException;
import kr.joyful.doit.service.member.MemberNotFoundException;
import kr.joyful.doit.service.team.TeamAlreadyExistsException;
import kr.joyful.doit.service.team.TeamNotFoundException;
import kr.joyful.doit.web.member.NotOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String boardNotFoundException(BoardNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cardNotFoundException(CardNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(CardListNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cardListNotFoundException(CardListNotFoundException e) {
        return e.getMessage();
    }



    @ExceptionHandler(BoardAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String boardAlreadyExistsException(BoardAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userNotFoundException(MemberNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(MemberAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String memberAlreadyExistsException(MemberAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(TeamNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String teamNotFoundException(TeamNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String teamAlreadyExistsException(TeamAlreadyExistsException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotOwnerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notOwnerException(NotOwnerException e) {
        return e.getMessage();
    }
}
