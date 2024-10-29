package hana.simple.userservice.global.exception.constant

import org.springframework.http.HttpStatus


enum class ErrorCode (
    val status: HttpStatus,
    val message: String,
){
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하는 중 에러가 발생했습니다.")
}
