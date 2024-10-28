package hana.simple.userservice.api.user.service

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserLogin
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity

interface UserService {
    fun join(userCreate: UserCreate): Long
    fun login(userLogin: UserLogin): UserEntity
    fun getUserInformation(userId: String): UserInformation
    fun changePassword(userId: String, userPasswordChange: UserPasswordChange): Long
    fun deleteUser(userId: String): Long
}
