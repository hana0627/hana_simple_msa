package hana.simple.boardservice.api.board.controller.request;


import lombok.Builder;

public record BoardCreate(
        String userId,
        String title,
        String content
) {

    @Builder
    public BoardCreate(String userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}
