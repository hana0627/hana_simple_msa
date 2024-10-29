package hana.simple.apigateway.config.jwt

interface JwtUtils {
    fun isExpired(token: String): Boolean
    fun getUserId(token: String): String
    fun isInValidated(token: String): Boolean
//    fun logout(request: HttpServletRequest, userId: String): Boolean
}