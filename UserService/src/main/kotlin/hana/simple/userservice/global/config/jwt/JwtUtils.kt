package hana.simple.userservice.global.config.jwt

import hana.simple.userservice.api.user.domain.UserEntity
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface JwtUtils {
    fun generateToken(request: HttpServletRequest, response: HttpServletResponse, userEntity: UserEntity): String
//    fun isExpired(token: String): Boolean
//    fun getUserId(token: String): String
//    fun isInValidated(token: String): Boolean
//    fun logout(request: HttpServletRequest, userId: String): Boolean
}