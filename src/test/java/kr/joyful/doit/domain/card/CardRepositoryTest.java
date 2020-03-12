package kr.joyful.doit.domain.card;

import kr.joyful.doit.domain.board.Board;
import kr.joyful.doit.domain.board.BoardRepository;
import kr.joyful.doit.domain.cardList.CardList;
import kr.joyful.doit.domain.cardList.CardListRepository;
import kr.joyful.doit.domain.member.Member;
import kr.joyful.doit.domain.member.MemberRepository;
import kr.joyful.doit.domain.member.MemberRole;
import kr.joyful.doit.domain.team.Team;
import kr.joyful.doit.domain.team.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CardListRepository cardListRepository;

    @Autowired
    EntityManager em;


    Board boardA;
    Board boardB;
    CardList listA;
    CardList listB;
    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("userA")
                .password("1234")
                .role(MemberRole.MEMBER)
                .email("userA@email.com")
                .build();
        memberRepository.save(member);

        Team team = Team.create("teamA", "this is teamA", member, 0);
        teamRepository.save(team);

        Team teamB = Team.create("teamB", "this is teamB", member, 1);
        teamRepository.save(teamB);

        boardA = Board.create(team, "boardA", "this is boardA");
        boardRepository.save(boardA);

        boardB = Board.create(teamB, "boardB", "this is boardB");
        boardRepository.save(boardB);

        listA = CardList.createCardList(boardA, "ListA", 0);
        cardListRepository.save(listA);

        listB = CardList.createCardList(boardB, "ListB", 0);
        cardListRepository.save(listB);
    }

    @Test
    void findCardsByBoard() {
        //given
        String cardAName = "testA";
        String cardADesc = "this is test card A";
        Card cardA = Card.create(cardAName, cardADesc, CardStatus.ACTIVATE, listA, 0);

        String cardBName = "testB";
        String cardBDesc = "this is test card B";
        Card cardB = Card.create(cardBName, cardBDesc, CardStatus.ACTIVATE, listA, 1);
        cardRepository.save(cardA);
        cardRepository.save(cardB);

        String cardCName = "testC";
        String cardCDesc = "this is test card D";
        Card cardC = Card.create(cardCName, cardCDesc, CardStatus.ACTIVATE, listB, 0);

        String cardDName = "testD";
        String cardDDesc = "this is test card D";
        Card cardD = Card.create(cardDName, cardDDesc, CardStatus.ACTIVATE, listB, 1);
        cardRepository.save(cardC);
        cardRepository.save(cardD);

        em.flush();
        em.clear();

        //when
        List<Card> cardsByBoard = cardRepository.findCardsByBoard(boardA);
        Card findCardA = cardsByBoard.get(0);
        Card findCardB = cardsByBoard.get(1);

        //then
        assertEquals(2, cardsByBoard.size());

        assertEquals(findCardA.getTitle(), cardAName);
        assertEquals(findCardA.getDescription(), cardADesc);

        assertEquals(findCardB.getTitle(), cardBName);
        assertEquals(findCardB.getDescription(), cardBDesc);

        assertEquals(findCardA, cardA);
        assertEquals(findCardB, cardB);

    }


}