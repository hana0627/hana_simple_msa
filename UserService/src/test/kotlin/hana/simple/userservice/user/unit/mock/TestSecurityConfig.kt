package hana.simple.userservice.user.unit.mock

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
class TestSecurityConfig {

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

}
