package hana.simple.boardservice.api.board.service.impl;

import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.domain.BoardMapper;
import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.board.service.BoardService;
import hana.simple.boardservice.global.exception.ApplicationException;
import hana.simple.boardservice.global.exception.constant.ErrorCode;
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
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다."));
        return BoardInformation.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .createdId(board.getCreateId())
                .createdAt(board.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public Long create(String userId, BoardCreate boardCreate) {
        //TODO - userService에 userId가 유효한지 확인
        BoardEntity board = BoardEntity.builder()
                .createId(userId)
                .title(boardCreate.title())
                .content(boardCreate.content())
                .build();
        return boardRepository.save(board).getId();
    }

    @Override
    @Transactional
    public Long update(String userId, BoardUpdate boardUpdate) {
        //TODO - userService에 userId가 유효한지 확인
        BoardEntity board = boardRepository.findById(boardUpdate.boardId()).orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다."));

        if (userId.equals(board.getCreateId())) {
            board.updateByBoardUpdate(boardUpdate); // 더티체킹으로 update
            return board.getId();
        }
        throw new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 글만 수정 가능합니다.");
    }

    @Override
    @Transactional
    public Long delete(String userId, Long boardId) {
        //TODO - userService에 userId가 유효한지 확인
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다."));

        if(userId.equals(board.getCreateId())) {
            boardRepository.delete(board);
            return boardId;
        }
        throw new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 글만 삭제 가능합니다.");
    }


}
