package kr.joyful.doit.service.team;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException(Long teamId) {
        super("해당하는 팀을 찾을 수 없습니다. id : " +teamId);
    }
}
