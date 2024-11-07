package hana.simple.userservice.user.unit.mock.jwt

import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.global.config.jwt.JwtUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class FakeJwtUtils() : JwtUtils {


    private val secretKey: String = "testSecret.01234567890.testSecrettest0987654321"

    private val expiredMs: Long = 1800

    private val refreshMs: Long = 5000
    override fun generateToken(
        request: HttpServletRequest,
        response: HttpServletResponse,
        userEntity: UserEntity,
    ): String {
        return "BEARER jwtToken"
    }
}