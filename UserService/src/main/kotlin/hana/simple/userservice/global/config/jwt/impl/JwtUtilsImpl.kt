package hana.simple.userservice.global.config.jwt.impl

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
        userId: String,
        userName: String,
    ): String {
        if (secretKey == null || expiredMs == null) {
            throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "SecretKey 혹은 expiredMs가 존재하지 않습니다.")
        }
        //TODO redis 캐싱
        //TODO login log 저장

        return createToken(secretKey, userId, userName, expiredMs)
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




//    override fun isExpired(token: String): Boolean {
//        val expiredDate: Date = extreactClaims(token).expiration
//        return expiredDate.before(Date())
//    }
//
//    override fun getUserId(token: String): String {
//        val claims: Claims = extreactClaims(token)
//        return claims["userId"].toString()
//    }
//
//    // 토큰변조확인
//    override fun isInValidated(token: String): Boolean {
//        // 토큰을 (Header, Payload, Signature)으로 분할
//        val chunks: List<String> = token.split(".")
//
//        // 시그니처 추출
//        val signature = chunks[2]
//
//        // 헤더+시그니처를 암호화
//        val headerAndClaims = "${chunks[0]}.${chunks[1]}"
//        val expectedSignature = generateSignature(headerAndClaims)
//
//        // 헤더+시그너처를 암호화한 값과 토큰의 Signature가 일치하는지 여부 확인
//        return signature != expectedSignature
//    }
//
//
//    override fun logout(request: HttpServletRequest, userId: String): Boolean {
//        TODO("Not yet implemented")
//        return true
//    }


    /**
     * 토큰 claims 정보 추출
     */
//    private fun extreactClaims(token: String): Claims {
//        if (secretKey == null || expiredMs == null) {
//            throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "key 혹은 expiredMs가 존재하지 않습니다.")
//        }
//
//        //claim = {userId=hanana0627, userName=박하나, iat=time, exp=time}
//        return Jwts.parserBuilder().setSigningKey(getKey(secretKey))
//            .build().parseClaimsJws(token).body
//    }


    /**
     * 헤더와 시그니처를 통해서 Signature 생성
     */
//    private fun generateSignature(headerAndClaims: String): String {
//        if (secretKey == null || expiredMs == null) {
//            throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "key 혹은 expiredMs가 존재하지 않습니다.")
//        }
//
//        // 시크릿 키를 바이트 배열로 변환
//        val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
//
//        // HMAC-SHA256 알고리즘을 사용하여 Mac 객체 초기화
//        val hmacSha256 = Mac.getInstance("HmacSHA256")
//        val secretKeySpec = SecretKeySpec(keyBytes, "HmacSHA256")
//        hmacSha256.init(secretKeySpec)
//
//        // 입력 값을 사용하여 서명 생성
//        val signatureBytes = hmacSha256.doFinal(headerAndClaims.toByteArray(Charsets.UTF_8))
//
//        // Base64 인코딩하여 문자열로 반환 (패딩 없는 Base64 인코딩 사용)
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes)
//    }

}

