package hana.simple.boardservice.api.board.controller;

import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.service.BoardService;
import hana.simple.boardservice.global.response.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/v2/board")
    public APIResponse<List<BoardInformation>> boards() {
        return APIResponse.success(boardService.getAllBoards());
    }

    @GetMapping("/v2/board/{boardId}")
    public APIResponse<BoardInformation> boardInfomation (
            @PathVariable Long boardId
    ) {
        return APIResponse.success(boardService.getBoard(boardId));
    }

    @PostMapping("/v2/board")
    public APIResponse<Long> boardCrete(
        @RequestBody BoardCreate boardCreate
    ) {
        return APIResponse.success(boardService.create(boardCreate));
    }

    @PatchMapping("/v2/board")
    public APIResponse<Long> boardUpdate(
            @RequestBody BoardUpdate boardUpdate
    ) {
        return APIResponse.success(boardService.update(boardUpdate));
    }

    @DeleteMapping("/v2/board/{boardId}")
    public APIResponse<Long> boardUpdate(
            @PathVariable Long boardId
    ) {
        return APIResponse.success(boardService.delete(boardId));
    }

}
