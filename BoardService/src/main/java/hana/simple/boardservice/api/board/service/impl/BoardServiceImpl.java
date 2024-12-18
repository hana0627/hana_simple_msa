package hana.simple.boardservice.api.board.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.domain.BoardMapper;
import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.board.service.BoardService;
import hana.simple.boardservice.api.feignclinet.UserServiceClient;
import hana.simple.boardservice.api.message.KafkaProducer;
import hana.simple.boardservice.global.exception.ApplicationException;
import hana.simple.boardservice.global.exception.constant.ErrorCode;
import hana.simple.boardservice.global.response.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final KafkaProducer kafkaProducer;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;


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
    public Long create(String authorization, String userId, BoardCreate boardCreate) {

        APIResponse<Boolean> writeable = userServiceClient.writeable(authorization, userId);
        if(writeable.getResult()) {
            BoardEntity board = BoardEntity.builder()
                    .createId(userId)
                    .title(boardCreate.title())
                    .content(boardCreate.content())
                    .build();

            BoardEntity savedBoard = boardRepository.save(board);

            try {
                sendCreateMessage(userId, savedBoard.getId().toString());
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "서버 통신간 에러 발생");
            }
            return savedBoard.getId();
        }
        throw new ApplicationException(ErrorCode.BOARD_MAX_COUNT, "최대 작성 가능한 게시글을 초과했습니다.");
    }

    @Override
    @Transactional
    public Long update(String userId, BoardUpdate boardUpdate) {
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
        BoardEntity board = boardRepository.findById(boardId).orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다."));

        if(userId.equals(board.getCreateId())) {
            boardRepository.delete(board);
            try {
                sendDeleteMessage(userId, boardId.toString());
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "서버 통신간 에러 발생");
            }

            return boardId;
        }

        throw new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 글만 삭제 가능합니다.");
    }

    private <T> void sendCreateMessage (String userId, String boardId) throws Exception {
        if(!StringUtils.hasText(userId)) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND, "회원정보를 찾을 수 없습니다.");
        }
        String key = "boardId"+boardId;
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        String message = objectMapper.writeValueAsString(map);

        kafkaProducer.sendMessage("hana_sample_create", key, message);
    }

    private <T> void sendDeleteMessage (String userId, String boardId) throws Exception {
        if(!StringUtils.hasText(userId)) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND, "회원정보를 찾을 수 없습니다.");
        }
        String key = "boardId"+boardId;
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        String message = objectMapper.writeValueAsString(map);

        kafkaProducer.sendMessage("hana_sample_delete", key, message);
    }

}
