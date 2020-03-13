package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.web.result.ApiResult;
import kr.joyful.doit.web.result.Result;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class MyBoard {

    public static ResponseEntity<ApiResult> build(List<Board> boards) {
        Map<String, List<BoardData>> map = new HashMap<>();
        boards.forEach(board -> {
            BoardData boardData = new BoardData(board);
            map.computeIfAbsent(boardData.getTeamName(), aLong -> new ArrayList<>()).add(boardData);
        });
        return Result.ok(ApiResult.blank().add("myBoard", map));
    }

    @Getter
    private static class BoardData {
        private Long boardId;
        private String title;
        private String description;
        private String teamName;

        BoardData(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
            this.description = board.getDescription();
            this.teamName = board.getTeam().getName();
        }
    }
}
