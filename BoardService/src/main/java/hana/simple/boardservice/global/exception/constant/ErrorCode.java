package hana.simple.boardservice.global.exception.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SAMPLE_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "예시용 에러코드 입니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    BOARD_MAX_COUNT(HttpStatus.CONFLICT, "최대 작성 가능한 게시글을 초과했습니다."),
    REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원정보를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "회원정보를 찾을 수 없습니다."),
    NOT_ME(HttpStatus.CONFLICT, "본인이 아닙니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
