package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.boardMember.BoardMember;
import kr.joyful.doit.domain.member.Member;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardResponseDto {
    private String title;
    private String description;
    private String teamName;
    private List<Member> members;

    public BoardResponseDto(Board board, List<Member> members) {
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.teamName = board.getTeam().getName();
        this.members = members;
    }
}
