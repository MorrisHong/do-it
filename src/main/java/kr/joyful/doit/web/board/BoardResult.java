package kr.joyful.doit.web.board;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.card.Card;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.web.result.ApiResult;
import kr.joyful.doit.web.result.Result;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardResult {
    public static ResponseEntity<ApiResult> build(Team team, Board board, List<Member> members, List<CardList> cardLists, List<Card> cards) {
        Map<String, Object> boardData = new HashMap<>();
        boardData.put("id", board.getId());
        boardData.put("title", board.getTitle());
        boardData.put("description", board.getDescription());

        List<MemberData> membersData = new ArrayList<>();
        for (Member member : members) {
            membersData.add(new MemberData(member));
        }

        List<CardListData> cardListsData = new ArrayList<>();
        Map<Long, List<Card>> cardsByList = new HashMap<>();
        for (Card card : cards) {
            cardsByList.computeIfAbsent(card.getCardList().getId(), aLong -> new ArrayList<>()).add(card);
        }

        for (CardList cardList : cardLists) {
            cardListsData.add(new CardListData(cardList, cardsByList.get(cardList.getId())));
        }

        ApiResult result = ApiResult.blank()
                .add("board", boardData)
                .add("members", membersData)
                .add("cardLists", cardListsData);

        Map<String, String> teamData = new HashMap<>();
        teamData.put("name", team.getName());
        result.add("team", teamData);

        return Result.ok(result);
    }

    @Getter
    private static class MemberData {
        private Long memberId;
        private String username;

        MemberData(Member member) {
            this.memberId = member.getId();
            this.username = member.getUsername();
        }
    }

    @Getter
    private static class CardListData {
        private Long id;
        private String name;
        private int position;
        private List<CardData> cards = new ArrayList<>();

        public CardListData(CardList cardList, List<Card> cards) {
            this.id = cardList.getId();
            this.name = cardList.getName();
            this.position = cardList.getPosition();
            if (cards != null) {
                for (Card card : cards) {
                    this.cards.add(new CardData(card));
                }
            }
        }
    }

    @Getter
    private static class CardData {
        private Long id;
        private String title;
        private String description;
        private int position;

        CardData(Card card) {
            this.id = card.getId();
            this.title = card.getTitle();
            this.description = card.getDescription();
            this.position = card.getPosition();
        }

    }

}