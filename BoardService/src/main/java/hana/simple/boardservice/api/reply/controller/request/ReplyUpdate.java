package hana.simple.boardservice.api.reply.controller.request;

import lombok.Builder;

public record ReplyUpdate(
        Long replyId,
        Long boardId,
        String content
) {
    @Builder
    public ReplyUpdate {}
}
