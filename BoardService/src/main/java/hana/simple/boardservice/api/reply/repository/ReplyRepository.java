package hana.simple.boardservice.api.reply.repository;

import hana.simple.boardservice.api.reply.domain.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findByBoardId(Long boardId);

    @Query("SELECT COALESCE(MAX(r.sequence), 0) FROM ReplyEntity r WHERE r.board.id = :boardId")
    Integer findMaxSequenceByBoardId(@Param("boardId") Long boardId);
}
