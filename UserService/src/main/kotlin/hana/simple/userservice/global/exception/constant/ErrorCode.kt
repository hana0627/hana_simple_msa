package hana.simple.userservice.global.exception.constant

import org.springframework.http.HttpStatus


enum class ErrorCode (
    val status: HttpStatus,
    val message: String,
){
    SAMPLE_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "예시용 에러코드 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원정보를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "중복된 아이디 입니다."),
    LOGIN_FAIL(HttpStatus.NOT_FOUND, "아이디가 없거나 비밀번호가 잘못되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하는 중 에러가 발생했습니다."),
    MAX_BOARD_COUNT(HttpStatus.CONFLICT, "작성가능한 게시글 수를 초과했습니다.")

}
