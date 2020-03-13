package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class MyBoard {

    private String teamName;
    private String title;
    private String description;

    public MyBoard(Board board) {
        this.teamName = board.getTeam().getName();
        this.title = board.getTitle();
        this.description = board.getDescription();
    }

}
