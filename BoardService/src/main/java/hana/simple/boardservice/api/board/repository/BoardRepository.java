package hana.simple.boardservice.api.board.repository;

import hana.simple.boardservice.api.board.domain.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
