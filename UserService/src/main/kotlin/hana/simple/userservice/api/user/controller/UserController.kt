package hana.simple.userservice.api.user.controller

import hana.simple.userservice.api.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.RestController

@RestController("/user-service")
@RequiredArgsConstructor
class UserController (
    private val userService: UserService,
){

}