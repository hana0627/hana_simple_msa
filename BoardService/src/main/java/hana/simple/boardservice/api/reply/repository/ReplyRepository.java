package hana.simple.boardservice.api.reply.repository;

import hana.simple.boardservice.api.reply.domain.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
}
