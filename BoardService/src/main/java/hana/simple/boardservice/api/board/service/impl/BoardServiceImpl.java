package hana.simple.boardservice.api.board.service.impl;

import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.domain.BoardMapper;
import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public List<BoardInformation> getAllBoards() {
        return boardRepository.findAll().stream().map(
            BoardMapper::boardInformation
        ).toList();
    }

    @Override
    public BoardInformation getBoard(Long boardId) {
        BoardEntity board = getOrExceptionById(boardId);
        return BoardInformation.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .createdId(board.getCreateId())
                .createdAt(board.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public Long create(BoardCreate boardCreate) {
        //TODO - userService에 userId가 유효한지 확인
        //TODO - DTO로 정보를 받기 전에 토큰정보를 이용할 수는 없는지
        BoardEntity board = BoardEntity.builder()
                .createId(boardCreate.userId())
                .title(boardCreate.title())
                .content(boardCreate.content())
                .build();
        return boardRepository.save(board).getId();
    }

    @Override
    @Transactional
    public Long update(BoardUpdate boardUpdate) {
        //TODO - userService에 userId가 유효한지 확인
        //TODO - DTO로 정보를 받기 전에 토큰정보를 이용할 수는 없는지
        BoardEntity board = getOrExceptionById(boardUpdate.boardId());
        board.updateByBoardUpdate(boardUpdate); // 더티체킹으로 update
        return board.getId();
    }

    @Override
    @Transactional
    public Long delete(Long boardId) {
        //TODO - userService에 userId가 유효한지 확인
        //TODO - DTO로 정보를 받기 전에 토큰정보를 이용할 수는 없는지
        BoardEntity board = getOrExceptionById(boardId);
        boardRepository.delete(board);
        return boardId;
    }


    private BoardEntity getOrExceptionById(Long id) {
        return boardRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
