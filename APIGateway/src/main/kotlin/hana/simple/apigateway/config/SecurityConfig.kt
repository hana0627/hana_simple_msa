package hana.simple.apigateway.config

import hana.simple.apigateway.config.jwt.JwtUtils
import hana.simple.apigateway.filter.JwtFiler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository


@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val jwtUtils: JwtUtils,
) {

    @Value("\${jwt.secret-key}")
    private val secretKey: String? = null

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeExchange { authorize ->
                authorize
                    .pathMatchers("user-service/hello").permitAll()
                    .pathMatchers("/user-service/v1/**").permitAll()
                    .pathMatchers("/user-service/v2/**").authenticated()
            }
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .addFilterAt(JwtFiler(secretKey, jwtUtils), SecurityWebFiltersOrder.AUTHENTICATION)
            .securityContextRepository(WebSessionServerSecurityContextRepository())
            .build()
    }
}
