package kr.joyful.doit.service.member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("해당하는 멤버를 찾을 수 없습니다. id : " + id);
    }

    public MemberNotFoundException(String usernameOrEmail) {
        super("해당하는 멤버를 찾을 수 없습니다. username or email : " + usernameOrEmail);
    }
}
