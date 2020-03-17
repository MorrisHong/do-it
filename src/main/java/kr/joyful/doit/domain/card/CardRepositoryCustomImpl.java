package kr.joyful.doit.domain.card;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.joyful.doit.domain.board.Board;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CardRepositoryCustomImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Card> findByCardId(Long cardId) {
        return Optional.ofNullable(queryFactory
                .select(QCard.card)
                .from(QCard.card)
                .where(
                        cardIdEq(cardId)
                )
                .fetchOne());
    }

    @Override
    public List<Card> findCardsByBoard(Board board) {
        return queryFactory
                .select(QCard.card)
                .from(QCard.card)
                .where(
                        boardEq(board)
                )
                .fetch();

    }

    private BooleanExpression boardEq(Board board) {
        return board == null ? null : QCard.card.cardList.board.eq(board);
    }

    private BooleanExpression cardIdEq(Long cardId) {
        return cardId == null ? null : QCard.card.id.eq(cardId);
    }

}
