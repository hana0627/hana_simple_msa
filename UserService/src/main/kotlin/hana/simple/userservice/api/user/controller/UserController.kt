package hana.simple.userservice.api.user.controller

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
class UserController (
    private val userService: UserService,
){
    //TODO return 객체정의

    @PostMapping("/create/user")
    fun createUser(@RequestBody userCreate: UserCreate): Map<String, Any> {
        return mapOf("result" to userService.join(userCreate))
    }

    @GetMapping("/user/{userId}")
    fun getUserInformation(
        @PathVariable userId: String
    ) : Map<String, Any>{
        return mapOf("result" to userService.getUserInformation(userId))
    }

    @PatchMapping("/user/{userId}/password")
    fun changeUserPassword(
        @PathVariable userId: String,
        @RequestBody userPasswordChange : UserPasswordChange,
    ) : Map<String, Any>{
        return mapOf("result" to userService.changePassword(userId, userPasswordChange))
    }

    @DeleteMapping("/user/{userId}")
    fun deleteUser(
        @PathVariable userId: String,
    ) : Map<String, Any>{
        return mapOf("result" to userService.deleteUser(userId))
    }
}
