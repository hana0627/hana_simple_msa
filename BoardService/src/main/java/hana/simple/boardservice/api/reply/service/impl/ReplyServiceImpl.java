package hana.simple.boardservice.api.reply.service.impl;

import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.domain.ReplyEntity;
import hana.simple.boardservice.api.reply.repository.ReplyMapper;
import hana.simple.boardservice.api.reply.repository.ReplyRepository;
import hana.simple.boardservice.api.reply.service.ReplyService;
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
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Override
    public List<ReplyInformation> getReplys(Long boardId) {
        return replyRepository.findByBoardId(boardId).stream().map(
                ReplyMapper::replyInformation
        ).toList();
    }

    @Override
    @Transactional
    public Long create(String userId, ReplyCreate replyCreate) {
        //TODO UserService 통신
        
        BoardEntity board = getBoardOrExceptionByBoardId(replyCreate.boardId());
        Integer sequence = getMaxSequence(replyCreate.boardId());

        ReplyEntity reply = ReplyEntity.builder()
                .board(board)
                .content(replyCreate.content())
                .createId(userId)
                .sequence(sequence)
                .build();

        board.addReply(reply);

        return replyRepository.save(reply).getId();

    }

    @Override
    @Transactional
    public Long update(String userId, ReplyUpdate replyUpdate) {
        //TODO UserService 통신
        if(isExistBoard(replyUpdate.boardId())) {
            ReplyEntity reply = getReplyOrExceptionById(replyUpdate.replyId());
            if(userId.equals(reply.getCreateId())) {
                reply.updateReply(replyUpdate);
                return replyUpdate.replyId();
            }
        }
        throw new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 댓글만 수정 가능합니다.");
    }

    @Override
    @Transactional
    public Long delete(String userId, Long replyId) {
        //TODO UserService 통신
        ReplyEntity reply = getReplyOrExceptionById(replyId);
        if(userId.equals(reply.getCreateId())) {
            replyRepository.delete(reply);
            return replyId;
        }
        throw new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 댓글만 삭제 가능합니다.");
    }






    private Boolean isExistBoard(Long boardId) {
        if(boardRepository.findById(boardId).isPresent()) {
            return true;
        }else {
            throw new ApplicationException(ErrorCode.BOARD_NOT_FOUND,"게시글이 존재하지 않습니다.");
        }
    }

    private ReplyEntity getReplyOrExceptionById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(() -> new ApplicationException(ErrorCode.REPLY_NOT_FOUND, "댓글이 존재하지 않습니다."));
    }

    private BoardEntity getBoardOrExceptionByBoardId(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new ApplicationException(ErrorCode.BOARD_NOT_FOUND,"게시글이 존재하지 않습니다."));
    }

    private Integer getMaxSequence(Long boardId) {
        return replyRepository.findMaxSequenceByBoardId(boardId);
    }
}
