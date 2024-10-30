package hana.simple.boardservice.api.board.service;


import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;

import java.util.List;

public interface BoardService {

    List<BoardInformation> getAllBoards();

    BoardInformation getBoard(Long boardId);

    Long create(BoardCreate boardCreate);

    Long update(BoardUpdate boardUpdate);

    Long delete(Long boardId);
}
