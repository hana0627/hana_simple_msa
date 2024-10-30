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
    public Long create(ReplyCreate replyCreate) {
        BoardEntity board = getBoardOrExceptionByBoardId(replyCreate.boardId());
        Integer sequence = getMaxSequence(replyCreate.boardId());

        ReplyEntity reply = ReplyEntity.builder()
                .board(board)
                .content(replyCreate.content())
                .createId(replyCreate.userId())
                .sequence(sequence)
                .build();

        board.addReply(reply);

        return replyRepository.save(reply).getId();

    }

    @Override
    @Transactional
    public Long update(ReplyUpdate replyUpdate) {
        if(isExistBoard(replyUpdate.boardId())) {
            ReplyEntity reply = getReplyOrExceptionById(replyUpdate.replyId());
            reply.updateReply(replyUpdate);
        }
        return replyUpdate.replyId();
    }

    @Override
    @Transactional
    public Long delete(Long replyId) {
        ReplyEntity reply = getReplyOrExceptionById(replyId);
        replyRepository.delete(reply);
        return replyId;
    }






    private Boolean isExistBoard(Long boardId) {
        if(boardRepository.findById(boardId).isPresent()) {
            return true;
        }else {
            throw new RuntimeException();
        }
    }

    private ReplyEntity getReplyOrExceptionById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(RuntimeException::new);
    }

    private BoardEntity getBoardOrExceptionByBoardId(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(RuntimeException::new);
    }

    private Integer getMaxSequence(Long boardId) {
        return replyRepository.findMaxSequenceByBoardId(boardId);
    }
}
