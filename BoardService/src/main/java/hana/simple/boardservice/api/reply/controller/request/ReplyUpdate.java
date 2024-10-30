package hana.simple.boardservice.api.reply.controller.request;

import lombok.Builder;

public record ReplyUpdate(
        String userId,
        Long replyId,
        Long boardId,
        String content
) {
    @Builder
    public ReplyUpdate(String userId, Long replyId, Long boardId, String content) {
        this.userId = userId;
        this.replyId = replyId;
        this.boardId = boardId;
        this.content = content;
    }
}
