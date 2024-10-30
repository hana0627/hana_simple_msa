package hana.simple.boardservice.api.board.controller.request;

import lombok.Builder;

public record BoardUpdate(
        String userId,
        Long boardId,
        String title,
        String content
) {
    @Builder
    public BoardUpdate(String userId, Long boardId, String title, String content) {
        this.userId = userId;
        this.boardId = boardId;
        this.title = title;
        this.content = content;
    }
}
