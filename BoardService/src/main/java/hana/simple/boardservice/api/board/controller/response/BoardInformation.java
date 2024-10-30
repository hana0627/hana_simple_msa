package hana.simple.boardservice.api.board.controller.response;

import hana.simple.boardservice.api.board.domain.BoardEntity;
import lombok.Builder;

import java.time.LocalDateTime;

public record BoardInformation(
        String title,
        String content,
        String createdId,
        LocalDateTime createdAt
) {

    @Builder
    public BoardInformation(String title, String content, String createdId, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.createdId = createdId;
        this.createdAt = createdAt;
    }

}
