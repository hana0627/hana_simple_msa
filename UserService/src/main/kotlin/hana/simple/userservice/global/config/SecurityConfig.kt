package hana.simple.userservice.global.config

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

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
            .build()


    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { it.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations().toString(), "/favicon.ico") }
    }
}