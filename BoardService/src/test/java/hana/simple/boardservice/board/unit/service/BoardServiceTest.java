package hana.simple.boardservice.board.unit.service;

import hana.simple.boardservice.api.board.controller.request.BoardCreate;
import hana.simple.boardservice.api.board.controller.request.BoardUpdate;
import hana.simple.boardservice.api.board.controller.response.BoardInformation;
import hana.simple.boardservice.api.board.domain.BoardEntity;
import hana.simple.boardservice.api.board.repository.BoardRepository;
import hana.simple.boardservice.api.board.service.impl.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    void 글_전체_조회가_성공한다() {
        //given
        BoardEntity board1 = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");
        BoardEntity board2 = BoardEntity.from(2L, "title2", "content2", Collections.emptyList(), "hanana");
        // `createdAt` 필드를 수동으로 설정
        LocalDateTime now = LocalDateTime.now();
        ReflectionTestUtils.setField(board1, "createdAt", now);
        ReflectionTestUtils.setField(board2, "createdAt", now);

        BoardInformation information1 = new BoardInformation(board1.getTitle(), board1.getContent(), board1.getCreateId(), board1.getCreatedAt());
        BoardInformation information2 = new BoardInformation(board2.getTitle(), board2.getContent(), board2.getCreateId(), board2.getCreatedAt());
        given(boardRepository.findAll()).willReturn(List.of(board1,board2));

        //when
        List<BoardInformation> result = boardService.getAllBoards();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(information1);
        assertThat(result.get(1)).isEqualTo(information2);

        then(boardRepository).should().findAll();
    }

    @Test
    void 글_1건_조회가_성공한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));

        //when
        BoardInformation result = boardService.getBoard(board.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo(board.getTitle());
        assertThat(result.content()).isEqualTo(board.getContent());
        assertThat(result.createdId()).isEqualTo(board.getCreateId());
        assertThat(result.createdAt()).isEqualTo(board.getCreatedAt());

    }
    @Test
    void 글_1건_조회시_없는_boardId를_사용하면_예외가_발생한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title1", "content1", Collections.emptyList(), "hanana");
        given(boardRepository.findById(9999L)).willThrow(new RuntimeException());

        //when & then
        assertThrows(RuntimeException.class, () -> boardService.getBoard(9999L));
    }

    @Test
    void 올바른_정보_입력시_글작성에_성공한다() {
        //given
        BoardCreate boardCreate = new BoardCreate("hanana","title","conetent");
        BoardEntity board = BoardEntity.from(1L, "title", "hanana", Collections.emptyList(), "hanana");


        given(boardRepository.save(any(BoardEntity.class))).willReturn(board);

        //when
        Long result = boardService.create(boardCreate);

        //then
        assertThat(result).isEqualTo(board.getId());
        then(boardRepository).should().save(any(BoardEntity.class));
    }


    @Test
    void 게시글_수정이_성공한다() {
        //given
        BoardUpdate boardUpdate = new BoardUpdate("hanana",1L,"updateTile","updateContent");
        BoardEntity board = BoardEntity.from(1L, "title", "content", Collections.emptyList(), "hanana");
        given(boardRepository.findById(boardUpdate.boardId())).willReturn(Optional.of(board));


        //when
        Long result = boardService.update(boardUpdate);


        //then
        assertThat(result).isNotNull();
        assertThat(board.getTitle()).isEqualTo(boardUpdate.title());
        assertThat(board.getContent()).isEqualTo(boardUpdate.content());
    }

    @Test
    void 없는_게시글에_대한_수정요청시_에러가_발생한다() {
        //given
        BoardUpdate boardUpdate = new BoardUpdate("hanana",9999L,"updateTile","updateContent");
        BoardEntity board = BoardEntity.from(1L, "title", "content", Collections.emptyList(), "hanana");
        given(boardRepository.findById(boardUpdate.boardId())).willThrow(new RuntimeException());


        //when && then
        assertThrows(RuntimeException.class, () -> boardService.update(boardUpdate));
    }

    @Test
    void 게시글_삭제가_성공한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title", "content", Collections.emptyList(), "hanana");
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));


        //when
        Long result = boardService.delete(board.getId());


        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(board.getId());
        then(boardRepository).should().findById(board.getId());
        then(boardRepository).should().delete(board);
    }


    @Test
    void 없는_게시글에_대한_삭제요청시_에러가_발생한다() {
        //given
        BoardEntity board = BoardEntity.from(1L, "title", "content", Collections.emptyList(), "hanana");
        given(boardRepository.findById(9999L)).willThrow(new RuntimeException());


        //when && then
        assertThrows(RuntimeException.class, () -> boardService.delete(9999L));
    }

}
