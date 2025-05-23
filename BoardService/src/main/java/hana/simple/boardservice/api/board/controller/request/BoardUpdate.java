package hana.simple.boardservice.api.board.controller.request;

import lombok.Builder;

public record BoardUpdate(
        Long boardId,
        String title,
        String content
) {
    @Builder
    public BoardUpdate {}
}
