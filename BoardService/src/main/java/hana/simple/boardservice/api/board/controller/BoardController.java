package hana.simple.boardservice.api.board.controller;

import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/v2/board")
    public Map<String, Object> boards() {
        List<BoardInformation> result = boardService.getAllBoards();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @GetMapping("/v2/board/{boardId}")
    public Map<String, Object> boardInfomation(
            @PathVariable Long boardId
    ) {
        BoardInformation result = boardService.getBoard(boardId);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @PostMapping("/v2/board")
    public Map<String, Object> boardCrete(
        @RequestBody BoardCreate boardCreate
    ) {
        Long result = boardService.create(boardCreate);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @PatchMapping("/v2/board")
    public Map<String, Object> boardUpdate(
            @RequestBody BoardUpdate boardUpdate
    ) {
        Long result = boardService.update(boardUpdate);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

    @DeleteMapping("/v2/board/{boardId}")
    public Map<String, Object> boardUpdate(
            @PathVariable Long boardId
    ) {
        Long result = boardService.delete(boardId);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return resultMap;
    }

}
