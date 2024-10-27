package hana.simple.userservice.global.exception

import hana.simple.userservice.global.exception.constant.ErrorCode
import org.springframework.http.HttpStatus

class ApplicationException(
    private val errorCode: ErrorCode,
    override val message: String? = null
) : RuntimeException() {


    val getErrorCode: ErrorCode
        get() = errorCode


    val getMessage: String
        get() = message ?: errorCode.message
}
