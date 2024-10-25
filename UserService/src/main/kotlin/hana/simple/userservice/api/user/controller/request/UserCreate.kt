package hana.simple.userservice.api.user.controller.request

import hana.simple.userservice.api.common.domain.constant.Gender

data class UserCreate (
    val userId: String,
    val userName: String,
    val password: String,
    val phoneNumber: String,
    val gender: Gender,
){

    companion object {
        fun fixture(
            userId: String = "hanana0627",
            userName: String = "박하나",
            password: String = "password1234",
            phoneNumber: String = "01012345678",
            gender: Gender = Gender.F,
        ) : UserCreate {
            return UserCreate(
                userId = userId,
                userName = userName,
                password = password,
                phoneNumber = phoneNumber,
                gender = gender)
        }
    }
}
