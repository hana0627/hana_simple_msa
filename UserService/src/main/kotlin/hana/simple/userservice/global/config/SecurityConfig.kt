package hana.simple.userservice.global.config

import hana.simple.userservice.global.config.filter.JwtFiler
import hana.simple.userservice.global.config.jwt.JwtUtils
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig (
    private val jwtUtils: JwtUtils,
){
    @Value("\${jwt.secret-key}")
    private val secretKey: String? = null

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { csrf -> csrf.disable() }


            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/hello").permitAll()
                    .requestMatchers("/v1/**").permitAll()
                    .requestMatchers("/v2/**").authenticated()
            }
            .formLogin { form -> form.disable() }
            .httpBasic { b -> b.disable() }

            .addFilterBefore(
                JwtFiler(secretKey = secretKey, jwtUtils = jwtUtils),
                UsernamePasswordAuthenticationFilter::class.java
            )

            .build()


    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { it.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations().toString(), "/favicon.ico") }
    }
}