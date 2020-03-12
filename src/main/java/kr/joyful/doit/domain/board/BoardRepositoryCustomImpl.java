package kr.joyful.doit.domain.board;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Strings;
import kr.joyful.doit.domain.team.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Board> findBoardByTitleAndTeam(String title, Team team) {
        return Optional.ofNullable(queryFactory
                .select(QBoard.board)
                .from(QBoard.board)
                .where(
                        titleEq(title),
                        teamEq(team)
                )
                .fetchOne());
    }

    private BooleanExpression teamEq(Team team) {
        return team == null ? null : QBoard.board.team.eq(team);
    }

    private BooleanExpression titleEq(String title) {
        return StringUtils.hasText(title) ? QBoard.board.title.eq(title) : null;
    }
}
