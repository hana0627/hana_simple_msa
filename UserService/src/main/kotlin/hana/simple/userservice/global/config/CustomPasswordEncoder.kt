package hana.simple.userservice.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class CustomPasswordEncoder() : PasswordEncoder{

    private val passwordEncoder = BCryptPasswordEncoder()


    override fun encode(rawPassword: CharSequence?): String {
        return passwordEncoder.encode(rawPassword);
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
