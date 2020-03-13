package kr.joyful.doit.domain.cardList;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardListRepository extends JpaRepository<CardList, Long> {
    List<CardList> findByBoardId(Long boardId);
}
