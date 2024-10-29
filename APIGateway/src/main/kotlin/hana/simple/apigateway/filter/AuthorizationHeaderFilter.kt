package hana.simple.apigateway.filter

import hana.simple.apigateway.config.jwt.JwtUtils
import hana.simple.apigateway.filter.AuthorizationHeaderFilter.Companion.Config
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@RequiredArgsConstructor
class AuthorizationHeaderFilter(
    private val jwtUtils: JwtUtils,
) : AbstractGatewayFilterFactory<Config>(Config::class.java) {
    @Value("\${jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${jwt.token.expired-time-ms}")
    private val expiredMs: Long? = null


    private val log = LoggerFactory.getLogger(javaClass)


    override fun apply(config: Config?): GatewayFilter {
        return (GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val request: ServerHttpRequest = exchange.request

            if (!request.headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                return@GatewayFilter onError(exchange, "No authorization Header", HttpStatus.UNAUTHORIZED)
            }

            val authorizationHeader: String = request.headers[HttpHeaders.AUTHORIZATION]!![0]
            val jwt = authorizationHeader.replace("Bearer ", "")



            if (!isJwtValid(jwt)) {
                return@GatewayFilter onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED)
            }

            chain.filter(exchange)
        })
    }

    private fun onError(exchange: ServerWebExchange, err: String, httpstatus: HttpStatus): Mono<Void>? {
        println("AuthorizationHeaderFilter.onError")
        val response: ServerHttpResponse = exchange.response
        response.setStatusCode(httpstatus)

        log.error(err)
        return response.setComplete()

    }

    private fun isJwtValid(jwt: String): Boolean {
        var returnValue: Boolean = true;

        if (jwtUtils.isExpired(jwt)) {
            log.error("inValidated Token")
            returnValue = false
        }

        // 유효한 토큰인지 검증
        if (jwtUtils.isInValidated(jwt)) {
            log.error("inValidated Token")
            returnValue = false
        }
        return returnValue
    }

    companion object {
        class Config {
        }
    }

}
