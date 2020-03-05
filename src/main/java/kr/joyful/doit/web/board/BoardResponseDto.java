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
    private List<BoardMemberResponseDto> members = new ArrayList<>();


    public BoardResponseDto(List<BoardMember> boardMemberList) {
        Board board = boardMemberList.get(0).getBoard();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.teamName = board.getTeam().getName();
        for (BoardMember boardMember : boardMemberList) {
            this.members.add(new BoardMemberResponseDto(boardMember.getMember()));
        }
    }

    @Getter
    static class BoardMemberResponseDto {
        private String username;
        private String email;

        public BoardMemberResponseDto(Member member) {
            this.username = member.getUsername();
            this.email = member.getEmail();
        }
    }
}
