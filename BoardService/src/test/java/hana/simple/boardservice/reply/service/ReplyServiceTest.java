package hana.simple.boardservice.reply.service;

import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.domain.ReplyEntity;
import hana.simple.boardservice.api.reply.repository.ReplyMapper;
import hana.simple.boardservice.api.reply.repository.ReplyRepository;
import hana.simple.boardservice.api.reply.service.impl.ReplyServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyServiceImpl replyService;

    @Test
    void 댓글_조회가_성공한다() {
        //given
        BoardEntity board1 = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");

        ReplyEntity reply1 = ReplyEntity.from(board1, "reply1", "danbi", 1L, 1);
        ReplyEntity reply2 = ReplyEntity.from(board1, "reply2", "sunny", 2L, 2);
        ReplyEntity reply3 = ReplyEntity.from(board1, "reply3", "chaehwa", 3L, 3);


        List<ReplyEntity> replies = List.of(reply1, reply2, reply3);
        List<ReplyInformation> replyInformations = replies.stream().map(ReplyMapper::replyInformation).toList();


        board1.updateReplies(replies);

        given(replyRepository.findByBoardId(board1.getId())).willReturn(replies);


        //when
        List<ReplyInformation> result = replyService.getReplys(board1.getId());

        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).isEqualTo(replyInformations);
        assertThat(result.get(0)).isEqualTo(replyInformations.get(0));
        assertThat(result.get(1)).isEqualTo(replyInformations.get(1));
        assertThat(result.get(2)).isEqualTo(replyInformations.get(2));

        then(replyRepository).should().findByBoardId(board1.getId());
    }

    @Test
    void 댓글_작성이_성공한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", new ArrayList<>(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "replyContent", "hanana", 1L, 1);


        ReplyCreate replyCreate = new ReplyCreate("hanana", board.getId(), "replyContent");

        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));
        given(replyRepository.findMaxSequenceByBoardId(board.getId())).willReturn(1);
        given(replyRepository.save(any(ReplyEntity.class))).willReturn(reply);

        //when
        Long result = replyService.create(replyCreate);

        //then
        then(boardRepository).should().findById(board.getId());
        then(replyRepository).should().findMaxSequenceByBoardId(board.getId());
        then(replyRepository).should().save(any(ReplyEntity.class));

        assertThat(result).isEqualTo(reply.getId());
        assertThat(board.getReplies().get(0).getBoard()).isEqualTo(reply.getBoard());
        assertThat(board.getReplies().get(0).getContent()).isEqualTo(reply.getContent());
    }

    @Test
    void 없는_게시글에_댓글_요청시_예외가_발생한다() {
        //given
        ReplyCreate replyCreate = new ReplyCreate("hanana", 9999L, "replyContent");

        given(boardRepository.findById(replyCreate.boardId())).willThrow(new RuntimeException());

        //when && then
        assertThrows(RuntimeException.class, () -> replyService.create(replyCreate));
    }


    @Test
    void 게시글_수정이_성공한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", new ArrayList<>(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "replyContent", "hanana", 1L, 1);
        ReplyUpdate replyUpdate = new ReplyUpdate("hanana", 1L,1L, "updatedReplyContent");


        given(boardRepository.findById(replyUpdate.boardId())).willReturn(Optional.of(board));
        given(replyRepository.findById(replyUpdate.replyId())).willReturn(Optional.of(reply));

        //when && then
        Long result = replyService.update(replyUpdate);

        then(boardRepository).should().findById(replyUpdate.boardId());
        then(replyRepository).should().findById(replyUpdate.replyId());

        assertThat(result).isEqualTo(1L);
        assertThat(reply.getContent()).isEqualTo(replyUpdate.content());

    }

    @Test
    void 없는_게시글에_대한_댓글_수정요청시_에러가_발생한다() {
        //given
        ReplyUpdate replyUpdate = new ReplyUpdate("hanana", 1L,9999L, "replyContent");

        given(boardRepository.findById(replyUpdate.boardId())).willThrow(new RuntimeException());

        //when && then
        assertThrows(RuntimeException.class, () -> replyService.update(replyUpdate));

        then(boardRepository).should().findById(replyUpdate.boardId());

    }
    
    @Test
    void 없는_댓글에_대한_댓글_수정요청시_에러가_발생한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", new ArrayList<>(), "hanana");
        ReplyUpdate replyUpdate = new ReplyUpdate("hanana", 9999L,1L, "replyContent");

        given(boardRepository.findById(replyUpdate.boardId())).willReturn(Optional.of(board));
        given(replyRepository.findById(replyUpdate.replyId())).willThrow(new RuntimeException());

        //when && then
        assertThrows(RuntimeException.class, () -> replyService.update(replyUpdate));

        then(boardRepository).should().findById(replyUpdate.boardId());
        then(replyRepository).should().findById(replyUpdate.replyId());

    }

    @Test
    void 댓글_삭제가_성공한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", new ArrayList<>(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "replyContent", "hanana", 1L, 1);


        given(replyRepository.findById(reply.getId())).willReturn(Optional.of(reply));

        //when && then
        Long result = replyService.delete(reply.getId());

        then(replyRepository).should().findById(reply.getId());

        assertThat(result).isEqualTo(1L);
        then(replyRepository).should().delete(reply);

    }

    @Test
    void 없는_댓글에_대한_댓글_삭제요청시_에러가_발생한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", new ArrayList<>(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "replyContent", "hanana", 1L, 1);

        given(replyRepository.findById(9999L)).willThrow(new RuntimeException());


        //when && then
        assertThrows(RuntimeException.class, () -> replyService.delete(9999L));
    }

}