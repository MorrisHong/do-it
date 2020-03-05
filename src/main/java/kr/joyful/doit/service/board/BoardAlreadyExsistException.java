package kr.joyful.doit.service.board;

public class BoardAlreadyExsistException extends RuntimeException {
    public BoardAlreadyExsistException() {
        super("이미 존재하는 보드입니다.");
    }
}
