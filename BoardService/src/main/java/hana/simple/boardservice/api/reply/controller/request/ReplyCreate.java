package hana.simple.boardservice.api.reply.controller.request;


import lombok.Builder;

public record ReplyCreate(
        String userId,
        Long boardId,
        String content
) {

    @Builder
    public ReplyCreate(String userId, Long boardId, String content) {
        this.userId = userId;
        this.boardId = boardId;
        this.content = content;
    }
}
