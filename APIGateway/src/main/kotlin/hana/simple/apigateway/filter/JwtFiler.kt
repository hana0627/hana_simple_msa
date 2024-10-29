package hana.simple.apigateway.filter

import hana.simple.apigateway.config.jwt.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


class JwtFiler(
    private val secretKey: String?,
    private val jwtUtils: JwtUtils,
) : WebFilter {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val response = exchange.response

        // permitAll으로 지정된 경로에 대해서는 jwt필터처리 수행x
        if (request.uri.path.contains("/v1")) {
            return chain.filter(exchange)
        }

        // get Header
        val header: String? = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (header == null || !header.startsWith("Bearer ")) {
            log.info("Error occurred while getting AUTHORIZATION Header")
            response.statusCode = HttpStatus.UNAUTHORIZED
            return response.setComplete()
        }

        // "Bearer " 이후의 문자열 추출
        val token: String = header.split(" ")[1].trim()

        if (secretKey == null) {
            log.error("key is null")
            response.statusCode = HttpStatus.UNAUTHORIZED
            return response.setComplete()
        }

        // 토큰 만료 확인
        if (jwtUtils.isExpired(token)) {
            log.error("Token is expired")
            response.statusCode = HttpStatus.UNAUTHORIZED
            return response.setComplete()
        }

        // 유효한 토큰인지 검증
        if (jwtUtils.isInValidated(token)) {
            log.error("Invalidated Token")
            response.statusCode = HttpStatus.UNAUTHORIZED
            return response.setComplete()
        }

        // 회원 아이디 추출
        val userId = jwtUtils.getUserId(token)
        val authentication = UsernamePasswordAuthenticationToken(userId, null, mutableListOf())
        val securityContext = SecurityContextImpl(authentication)

        return chain.filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
    }
}
