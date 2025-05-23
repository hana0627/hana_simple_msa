package hana.simple.boardservice.reply.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.reply.controller.ReplyController;
import hana.simple.boardservice.api.reply.controller.request.ReplyCreate;
import hana.simple.boardservice.api.reply.controller.request.ReplyUpdate;
import hana.simple.boardservice.api.reply.controller.response.ReplyInformation;
import hana.simple.boardservice.api.reply.domain.ReplyEntity;
import hana.simple.boardservice.api.reply.service.ReplyService;
import hana.simple.boardservice.global.exception.ApplicationException;
import hana.simple.boardservice.global.exception.constant.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplyController.class)
public class ReplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private ReplyService replyService;


    @Test
    void 댓글_조회가_성공한다() throws Exception {
        //given
        ReplyInformation reply1 = new ReplyInformation("reply1",1,"hanana", now());
        ReplyInformation reply2 = new ReplyInformation("reply2",2,"danbi", now());
        ReplyInformation reply3 = new ReplyInformation("reply3",3,"chaehwa", now());


        List<ReplyInformation> replies = List.of(reply1, reply2, reply3);

        given(replyService.getReplys(1L)).willReturn(replies);

        //when && then
        mvc.perform(get("/v2/reply/{board_id}",1L))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].content").value(reply1.content()))
                .andExpect(jsonPath("$.result[0].sequence").value(reply1.sequence()))
                .andExpect(jsonPath("$.result[0].createdId").value(reply1.createdId()))
                .andExpect(jsonPath("$.result[0].createdAt").exists())
                .andExpect(jsonPath("$.result[1].content").value(reply2.content()))
                .andExpect(jsonPath("$.result[1].sequence").value(reply2.sequence()))
                .andExpect(jsonPath("$.result[1].createdId").value(reply2.createdId()))
                .andExpect(jsonPath("$.result[1].createdAt").exists())
                .andExpect(jsonPath("$.result[2].content").value(reply3.content()))
                .andExpect(jsonPath("$.result[2].sequence").value(reply3.sequence()))
                .andExpect(jsonPath("$.result[2].createdId").value(reply3.createdId()))
                .andExpect(jsonPath("$.result[2].createdAt").exists())
                .andDo(print());

    }

    @Test
    void 댓글_작성이_성공한다() throws Exception {
        //given
        String userId = "hanana";
        ReplyCreate replyCreate = new ReplyCreate(1L,"content");

        given(replyService.create(userId, replyCreate)).willReturn(1L);

        String json = om.writeValueAsString(replyCreate);

        //when && then
        mvc.perform(post("/v2/reply")
                        .header("userId",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L))
                .andDo(print());


    }

    @Test
    void 없는_게시글에_댓글_요청시_예외가_발생한다() throws Exception {
        //given
        String userId = "hanana";
        ReplyCreate replyCreate = new ReplyCreate(1L,"content");

        given(replyService.create(userId, replyCreate)).willThrow(new ApplicationException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다."));

        String json = om.writeValueAsString(replyCreate);

        //when && then
        mvc.perform(post("/v2/reply")
                        .header("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("게시글이 존재하지 않습니다."))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name()))
                .andDo(print());

    }

    @Test
    void 게시글_수정이_성공한다() throws Exception {
        //given
        String userId = "hanana";
        ReplyUpdate replyUpdate = new ReplyUpdate(1L,1L, "updateContent");

        given(replyService.update(userId, replyUpdate)).willReturn(1L);

        String json = om.writeValueAsString(replyUpdate);

        //when && then
        mvc.perform(patch("/v2/reply")
                        .header("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L))
                .andDo(print());

    }


    @Test
    void 자신이_작성하지_않은_댓글_수정요청시_에러가_발생한다() throws Exception {
        //given
        String userId = "wrongUser";
        ReplyUpdate replyUpdate = new ReplyUpdate(1L,1L, "updateContent");

        given(replyService.update(userId, replyUpdate)).willThrow(new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 댓글만 수정 가능합니다."));

        String json = om.writeValueAsString(replyUpdate);

        //when && then
        mvc.perform(patch("/v2/reply")
                        .header("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("본인이 작성한 댓글만 수정 가능합니다."))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.CONFLICT.name()))
                .andDo(print());
    }


    @Test
    void 없는_게시글에_대한_댓글_수정요청시_에러가_발생한다() throws Exception {
        //given
        String userId = "hanana";
        ReplyUpdate replyUpdate = new ReplyUpdate(1L,1L, "updateContent");

        given(replyService.update(userId, replyUpdate)).willThrow(new ApplicationException(ErrorCode.BOARD_NOT_FOUND, "게시글이 존재하지 않습니다."));

        String json = om.writeValueAsString(replyUpdate);

        //when && then
        mvc.perform(patch("/v2/reply")
                        .header("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("게시글이 존재하지 않습니다."))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    void 없는_댓글에_대한_댓글_수정요청시_에러가_발생한다() throws Exception {
        //given
        String userId = "hanana";
        ReplyUpdate replyUpdate = new ReplyUpdate(1L,1L, "updateContent");

        given(replyService.update(userId, replyUpdate)).willThrow(new ApplicationException(ErrorCode.REPLY_NOT_FOUND, "댓글이 존재하지 않습니다."));

        String json = om.writeValueAsString(replyUpdate);

        //when && then
        mvc.perform(patch("/v2/reply")
                        .header("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("댓글이 존재하지 않습니다."))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name()))
                .andDo(print());
    }

    @Test
    void 댓글_삭제가_성공한다() throws Exception {
        //given
        String userId = "hanana";
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "reply1", "danbi", 1L, 1);

        given(replyService.delete(userId, reply.getId())).willReturn(1L);

        //when && then
        mvc.perform(delete("/v2/reply/{replyId}",reply.getId())
                        .header("userId",userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L))
                .andDo(print());
    }

    @Test
    void 자신이_작성하지_않은_댓글에_대한_댓글_삭제요청시_에러가_발생한다() throws Exception {
        //given
        String userId = "wrongUser";
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "reply1", "danbi", 1L, 1);

        given(replyService.delete(userId, reply.getId())).willThrow(new ApplicationException(ErrorCode.NOT_ME, "본인이 작성한 댓글만 삭제 가능합니다."));

        //when && then
        mvc.perform(delete("/v2/reply/{replyId}",reply.getId())
                        .header("userId",userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("본인이 작성한 댓글만 삭제 가능합니다."))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.CONFLICT.name()))
                .andDo(print());
    }


    
    @Test
    void 없는_댓글에_대한_댓글_삭제요청시_에러가_발생한다() throws Exception {
        //given
        String userId = "hanana";
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");
        ReplyEntity reply = ReplyEntity.from(board, "reply1", "danbi", 1L, 1);

        given(replyService.delete(userId, reply.getId())).willThrow(new ApplicationException(ErrorCode.REPLY_NOT_FOUND, "댓글이 존재하지 않습니다."));

        //when && then
        mvc.perform(delete("/v2/reply/{replyId}",reply.getId())
                        .header("userId",userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("댓글이 존재하지 않습니다."))
                .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name()))
                .andDo(print());
    }

}
