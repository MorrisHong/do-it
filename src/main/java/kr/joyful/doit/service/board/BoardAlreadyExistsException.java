package kr.joyful.doit.service.board;

public class BoardAlreadyExistsException extends RuntimeException {
    public BoardAlreadyExistsException() {
        super("이미 존재하는 보드입니다.");
    }
}
