package hana.simple.boardservice.api.reply.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.common.domain.AuditingFields;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class ReplyEntity extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer sequence;
    private String content;
    private String createId;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    @Setter
    private BoardEntity board;

    @Builder
    private ReplyEntity(Long id, BoardEntity board, String content, String createId,Integer sequence) {
        this.board = board;
        this.id = id;
        this.content = content;
        this.createId = createId;
        this.sequence = sequence;
    }

    public static ReplyEntity from(BoardEntity board, String content, String createId, Long id, Integer sequence) {
        return new ReplyEntity(id, board, content, createId, sequence);
    }

    public void updateReply(ReplyUpdate replyUpdate) {
        this.content = replyUpdate.content();
    }


}

