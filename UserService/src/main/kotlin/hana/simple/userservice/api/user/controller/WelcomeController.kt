package hana.simple.userservice.api.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WelcomeController {
    @GetMapping("/")
    fun welcome(): String {
        return "welcome this is userService"
    }

    @GetMapping("/hello")
    fun hello(): String {
        return "hello"
    }
}