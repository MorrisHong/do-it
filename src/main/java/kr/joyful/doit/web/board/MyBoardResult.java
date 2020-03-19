package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.web.result.ApiResult;
import kr.joyful.doit.web.result.Result;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Getter
public class MyBoardResult {

    public static ResponseEntity<ApiResult> build(List<Board> boards) {
        Map<String, List<Board>> collect = boards.stream().collect(groupingBy(board -> board.getTeam().getName()));
        List<BoardData> result = collect.values().stream().map(BoardData::new).collect(Collectors.toList());
//        Set<String> strings = collect.keySet();
//        List<BoardData> result = new ArrayList<>();
//        for (String string : strings) {
//            List<Board> boards1 = collect.get(string);
//            result.add(new BoardData(boards1));
//        }
        return Result.ok(ApiResult.blank().add("myBoard", result));
    }

    @Getter
    private static class BoardData {
        private String teamName;
        private List<BoardDataDetail> details = new ArrayList<>();

        public BoardData(List<Board> boards) {
            boards.forEach(board -> {
                this.teamName = board.getTeam().getName();
                this.details.add(new BoardDataDetail(board));
            });
        }

        @Getter
        private static class BoardDataDetail {
            private Long boardId;
            private String title;
            private String description;
            private String teamName;

            public BoardDataDetail(Board board) {
                this.boardId = board.getId();
                this.title = board.getTitle();
                this.description = board.getDescription();
                this.teamName = board.getTeam().getName();
            }
        }

    }
}
