package hana.simple.boardservice.api.reply.controller.response;

import lombok.Builder;

import java.time.LocalDateTime;

public record ReplyInformation(
        String content,
        Integer sequence,
        String createdId,
        LocalDateTime createdAt
) {

    @Builder
    public ReplyInformation(String content, Integer sequence, String createdId, LocalDateTime createdAt) {
        this.content = content;
        this.sequence = sequence;
        this.createdId = createdId;
        this.createdAt = createdAt;
    }

}
