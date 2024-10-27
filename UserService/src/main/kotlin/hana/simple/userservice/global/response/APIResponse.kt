package hana.simple.userservice.global.response

import hana.simple.userservice.global.exception.ApplicationException
import org.springframework.http.HttpStatus

data class APIResponse<T> (
    val resultCode: String, // 왠만하면 항상 200으로 고정
    val result: T,
) {

    companion object {
        fun <T> success(result: T) : APIResponse<T> {
            return APIResponse(HttpStatus.OK.name, result)
        }

        fun <T> error(result: T): APIResponse<String> {
            return if (result is ApplicationException) {
                APIResponse((result as ApplicationException).getErrorCode.status.name, result.getMessage)
            } else {
                APIResponse(HttpStatus.INTERNAL_SERVER_ERROR.name, "알 수 없는 예외가 발생했습니다.")
            }
        }

    }
}
