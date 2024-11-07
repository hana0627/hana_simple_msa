package hana.simple.boardservice.api.reply.controller.request;


import lombok.Builder;

public record ReplyCreate(
        Long boardId,
        String content
) {
    @Builder
    public ReplyCreate {}
}
