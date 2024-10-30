package hana.simple.boardservice.board.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hana.simple.boardservice.api.board.controller.BoardController;
import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @MockBean
    private BoardService boardService;


    @Test
    void 글_전체_조회가_성공한다() throws Exception {
        //given
        BoardInformation information1 = new BoardInformation("title1", "content1", "hanana", now());
        BoardInformation information2 = new BoardInformation("title2", "content2", "hanana", now());


        given(boardService.getAllBoards()).willReturn(List.of(information1, information2));

        // when && then
        mvc.perform(get("/v2/board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].title").value("title1"))
                .andExpect(jsonPath("$.result[0].content").value("content1"))
                .andExpect(jsonPath("$.result[0].createdId").value("hanana"))
                .andExpect(jsonPath("$.result[0].createdAt").exists())
                .andExpect(jsonPath("$.result[1].title").value("title2"))
                .andExpect(jsonPath("$.result[1].content").value("content2"))
                .andExpect(jsonPath("$.result[1].createdId").value("hanana"))
                .andExpect(jsonPath("$.result[1].createdAt").exists())
                .andDo(print());

        then(boardService).should().getAllBoards();

    }


    @Test
    void 글_1건_조회가_성공한다() throws Exception{
        //given
        BoardInformation information1 = new BoardInformation("title1", "content1", "hanana", now());
        given(boardService.getBoard(1L)).willReturn(information1);

        //when && then
        mvc.perform(get("/v2/board/{boardId}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("title1"))
                .andExpect(jsonPath("$.result.content").value("content1"))
                .andExpect(jsonPath("$.result.createdId").value("hanana"))
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andDo(print());

        then(boardService).should().getBoard(1L);
    }

    @Test
    void 글_1건_조회시_없는_boardId를_사용하면_예외가_발생한다() throws Exception{
        //TODO 예외객체 생성 후 재작성
        //given

        //when && then
    }

    @Test
    void 올바른_정보_입력시_글작성에_성공한다() throws Exception{
        //given
        BoardCreate boardCreate = new BoardCreate("hanana", "title","content");
        given(boardService.create(boardCreate)).willReturn(1L);

        String json = om.writeValueAsString(boardCreate);

        //when && then
        mvc.perform(post("/v2/board")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L))
                .andDo(print());

        then(boardService).should().create(boardCreate);

    }

    @Test
    void 게시글_수정이_성공한다() throws Exception{
        //given
        BoardUpdate boardUpdate = new BoardUpdate("hanana",1L,"updateTitle","content");

        String json = om.writeValueAsString(boardUpdate);

        given(boardService.update(boardUpdate)).willReturn(1L);


        //when && then
        mvc.perform(patch("/v2/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L))
                .andDo(print());

        then(boardService).should().update(boardUpdate);

    }

    @Test
    void 없는_게시글에_대한_수정요청시_에러가_발생한다() throws Exception{
        //TODO 예외객체 생성 후 재작성
        //given

        //when && then
    }

    @Test
    void 게시글_삭제가_성공한다() throws Exception{
        //given
        given(boardService.delete(1L)).willReturn(1L);

        //when && then
        mvc.perform(delete("/v2/board/{boardId}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(1L))
                .andDo(print());

        then(boardService).should().delete(1L);

    }

    @Test
    void 없는_게시글에_대한_삭제요청시_에러가_발생한다() throws Exception{
        //TODO 예외객체 생성 후 재작성
        //given

        //when && then
    }

}
