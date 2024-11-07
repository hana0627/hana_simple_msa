package hana.simple.boardservice.api.board.service;


import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;

import java.util.List;

public interface BoardService {

    List<BoardInformation> getAllBoards();

    BoardInformation getBoard(Long boardId);

    Long create(String userId, BoardCreate boardCreate);

    Long update(String userId, BoardUpdate boardUpdate);

    Long delete(String userId, Long boardId);
}
