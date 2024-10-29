package hana.simple.boardservice.api.board.service.impl;

import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private BoardRepository boardRepository;

}
