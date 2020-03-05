package kr.joyful.doit.service.board;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(Long boardId) {
        super("존재하지않는 보드입니다. id : " + boardId);
    }
}
