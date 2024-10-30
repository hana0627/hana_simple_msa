package hana.simple.boardservice.api.board.domain;

import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.common.domain.AuditingFields;
import hana.simple.boardservice.api.reply.domain.ReplyEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity  extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String createId;
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ReplyEntity> replies = new ArrayList<>();

    @Builder
    private BoardEntity(Long id, String content, String title, List<ReplyEntity> replies, String createId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createId = createId;
        this.replies = replies;
    }


    public static BoardEntity from(Long id, String title,String content,  List<ReplyEntity> replies, String createId) {
        return new BoardEntity(id, title, content, replies, createId);
//        this.id = id;
//        this.title = title;
//        this.content = content;
//        this.createId = createId;
//        this.replies = replies;
    }


    public void updateByBoardUpdate(BoardUpdate dto) {
        this.title = dto.title();
        this.content = dto.content();
    }
}
