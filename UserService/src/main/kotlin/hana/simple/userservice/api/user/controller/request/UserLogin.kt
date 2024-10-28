package hana.simple.userservice.api.user.controller.request

data class UserLogin (
    val userId: String,
    val password: String,      // 변경할 비밀번호
){
    companion object {
        fun fixture(
            userId: String = "hanana0627",
            password: String = "password1234",
        ) : UserLogin {
            return UserLogin(
                userId = userId,
                password = password,
                )
        }
    }
}
