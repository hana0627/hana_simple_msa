package hana.simple.userservice.api.user.controller.response

import hana.simple.userservice.api.common.domain.constant.Gender

data class UserInformation(
    val userId: String,
    val userName: String,
    val phoneNumber: String,
    val gender: Gender,
) {
    companion object {
        fun fixture(
            userId: String = "hanana0627",
            userName: String = "박하나",
            phoneNumber: String = "01012345678",
            gender: Gender = Gender.F,
        ): UserInformation {
            return UserInformation(
                userId = userId,
                userName = userName,
                phoneNumber = phoneNumber,
                gender = gender,
            )
        }
    }
}
