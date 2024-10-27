package hana.simple.userservice.global.exception.constant

import org.springframework.http.HttpStatus


enum class ErrorCode (
    val status: HttpStatus,
    val message: String,
){
    SAMPLE_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "예시용 에러코드 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원정보를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "중복된 아이디 입니다."),

}
