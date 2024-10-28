package hana.simple.userservice.user.unit.mock

import hana.simple.userservice.global.config.jwt.JwtUtils
import hana.simple.userservice.user.unit.mock.jwt.FakeJwtUtils
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
    @Bean
    fun jwtUtils(): JwtUtils {
        return FakeJwtUtils()
    }

}