package kr.joyful.doit.service.team;

public class TeamAlreadyExistsException extends RuntimeException {
    public TeamAlreadyExistsException(String name) {
        super("이미 존재하는 팀입니다. name : " + name);
    }
}
