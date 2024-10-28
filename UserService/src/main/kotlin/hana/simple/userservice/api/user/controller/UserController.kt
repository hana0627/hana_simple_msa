package hana.simple.userservice.api.user.controller

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.service.UserService
import hana.simple.userservice.global.response.APIResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
class UserController (
    private val userService: UserService,
){
    @PostMapping("/v1/create/user")
    fun createUser(@RequestBody userCreate: UserCreate): APIResponse<Long> {
        return APIResponse.success(userService.join(userCreate))
    }

    @GetMapping("/v2/user/{userId}")
    fun getUserInformation(
        @PathVariable userId: String
    ): APIResponse<UserInformation> {
        return APIResponse.success(userService.getUserInformation(userId))
    }

    @PatchMapping("/v2/user/{userId}/password")
    fun changeUserPassword(
        @PathVariable userId: String,
        @RequestBody userPasswordChange : UserPasswordChange,
    ): APIResponse<Long> {
        return APIResponse.success(userService.changePassword(userId, userPasswordChange))
    }

    @DeleteMapping("/v2/user/{userId}")
    fun deleteUser(
        @PathVariable userId: String,
    ): APIResponse<Long> {
        return APIResponse.success(userService.deleteUser(userId))
    }
}
