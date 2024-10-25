package hana.simple.userservice.api.user.controller.request

data class UserPasswordChange(
    val userId: String,
    val currentPassword: String, // 현재 비밀번호
    val newPassword: String,      // 변경할 비밀번호
    val confirmPassword: String    // 비밀번호 확인
) {

    companion object {
        fun fixture(
            userId: String = "hanana",
            currentPassword: String = "beforePassword",
            newPassword: String = "password1234",
            confirmPassword: String = "password1234",
        ): UserPasswordChange {
            return UserPasswordChange(
                userId = userId,
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword,
            )
        }
    }
}
