package hana.simple.boardservice.api.reply.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.common.domain.AuditingFields;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
    private BoardEntity board;
}

