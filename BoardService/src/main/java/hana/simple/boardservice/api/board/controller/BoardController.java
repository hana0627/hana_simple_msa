package hana.simple.boardservice.api.board.controller;

import hana.simple.boardservice.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private BoardService boardService;
}
