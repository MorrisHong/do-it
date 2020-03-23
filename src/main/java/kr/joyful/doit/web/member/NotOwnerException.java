package kr.joyful.doit.web.member;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException() {
        super("프로필을 수정할 수 있는 권한이 없습니다.");
    }
}
