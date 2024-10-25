package hana.simple.userservice.global.response

data class APIResponse<T> (
    val resultCode: String, // 왠만하면 항상 200으로 고정
    val result: T,
) {

    companion object {
        fun <T> success(result: T) : APIResponse<T> {
            return APIResponse("SUCCESS", result)
        }
        
        // 아래두개 안쓸수도 있음

        fun <T> clientError(result: T) : APIResponse<T> {
            return APIResponse("CLIENT ERROR", result)
        }
        fun <T> serverError(result: T) : APIResponse<T> {
            return APIResponse("SERVER ERROR", result)
        }
    }
}