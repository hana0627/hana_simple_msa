package hana.simple.boardservice.api.board.domain;

import hana.simple.boardservice.api.board.controller.response.BoardInformation;

public class BoardMapper {

    public static BoardInformation boardInformation(BoardEntity entity) {
        return new BoardInformation(
                entity.getTitle(),
                entity.getContent(),
                entity.getCreateId(),
                entity.getCreatedAt()
        );
    }

}
