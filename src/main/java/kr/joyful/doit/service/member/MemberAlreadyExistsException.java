package kr.joyful.doit.service.member;

public class MemberAlreadyExistsException extends RuntimeException {

    public MemberAlreadyExistsException() {
        super("이미 존재하는 사용자입니다.");
    }
}
