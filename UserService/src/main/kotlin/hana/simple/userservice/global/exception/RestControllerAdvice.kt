package hana.simple.userservice.global.exception

import hana.simple.userservice.global.response.APIResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestControllerAdvice {

    private val log = LoggerFactory.getLogger(RestControllerAdvice::class.java)

    @ExceptionHandler(ApplicationException::class)
    fun applicationHandler(error: ApplicationException) : APIResponse<String> {
        log.info("=== Application Exception occurred !! error : $error ===")
        return APIResponse.error(error)
    }

}
