package hana.simple.userservice.global.config.jwt.impl

import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.repository.UserCacheRepository
import hana.simple.userservice.api.user.repository.UserRepository
import hana.simple.userservice.global.config.jwt.JwtUtils
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

@Component
@RequiredArgsConstructor
class JwtUtilsImpl (
    private val userRepository: UserRepository,
    private val userCacheRepository: UserCacheRepository,
) : JwtUtils {
    @Value("\${jwt.secret-key}")
    private val secretKey: String? = null
    @Value("\${jwt.token.expired-time-ms}")
    private val expiredMs: Long? = null
    @Value("\${jwt.refresh.expired-time-ms}")
    private val refreshMs: Long? = null
    private val log = LoggerFactory.getLogger(javaClass)


    override fun generateToken(
        request: HttpServletRequest,
        response: HttpServletResponse,
        userEntity: UserEntity,
    ): String {
        if (secretKey == null || expiredMs == null) {
            throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "SecretKey 혹은 expiredMs가 존재하지 않습니다.")
        }
        userCacheRepository.setUser(userEntity)

        return createToken(secretKey, userEntity.userId, userEntity.userName, expiredMs)
    }
    /**
     * 서명키 생성
     */
    private fun getKey(key: String): Key {
        val keyByte: ByteArray = key.toByteArray(StandardCharsets.UTF_8)
        return Keys.hmacShaKeyFor(keyByte)
    }

    /**
     * jwt 토큰 생성
     */
    private fun createToken(
        secretKey: String,
        userId: String,
        userName: String,
        expiredMs: Long
    ): String {

        val claims: Claims = Jwts.claims()
        claims.put("userId", userId)
        claims.put("userName", userName)

        val token: String = "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiredMs * 1000))
            .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
            .compact()

        return token
    }



}

