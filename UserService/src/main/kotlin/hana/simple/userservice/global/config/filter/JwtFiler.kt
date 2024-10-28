package hana.simple.userservice.global.config.filter

import hana.simple.userservice.global.config.jwt.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

@RequiredArgsConstructor
class JwtFiler(
    private val secretKey: String?,
    private val jwtUtils: JwtUtils,
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        // permitAll으로 지정된 경로에 대해서는 jwt필터처리 수행x
        if (request.requestURL.contains("/v1")) {
            filterChain.doFilter(request, response)
        } else {
            // get Header
            val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

            if (header == null || !header.startsWith("Bearer ")) {
                log.info("Error occurred while getting AUTHORIZATION Header")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "jwt 토큰 정보가 없습니다")
                return
            }

            // "Bearer "이후의 문자열 추출
            val token: String = header.split(" ")[1].trim()

            if (secretKey == null) {
                log.error("key is null")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "secretKey가 존재하지 않습니다.")
                return
            }
            // 토큰만료확인
            if (jwtUtils.isExpired(token)) {
                log.error("key is expired")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "jwt 토큰이 만료되었습니다.")
                filterChain.doFilter(request, response)
            }
            // 유효한 토큰인지 검증
            if (jwtUtils.isInValidated(token)) {
                log.error("inValidated Token")
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰정보가 유효하지 않습니다.")
                filterChain.doFilter(request, response)
            }
            // 회원 아이디 추출
            val userId = jwtUtils.getUserId(token)

            val authentication = UsernamePasswordAuthenticationToken(userId, null, mutableListOf())
            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response)
        }
    }
}
