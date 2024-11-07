package hana.simple.boardservice.api.board.controller.request;


import lombok.Builder;

public record BoardCreate(
        String title,
        String content
) {

    @Builder
    public BoardCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
