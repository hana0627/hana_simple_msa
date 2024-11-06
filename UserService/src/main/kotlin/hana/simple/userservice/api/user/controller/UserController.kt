package hana.simple.userservice.api.user.controller

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserLogin
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.service.UserService
import hana.simple.userservice.global.config.jwt.JwtUtils
import hana.simple.userservice.global.response.APIResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
class UserController(
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
){
    @PostMapping("/v1/user")
    fun createUser(@RequestBody userCreate: UserCreate): APIResponse<Long> {
        return APIResponse.success(userService.join(userCreate))
    }

    @PostMapping("/v1/user/login")
    fun login(@RequestBody userLogin: UserLogin,
              request: HttpServletRequest,
              response: HttpServletResponse
    ): APIResponse<String> {
        val userEntity: UserEntity = userService.login(userLogin)
        val token: String = jwtUtils.generateToken(
            request = request,
            response= response,
            userEntity = userEntity)
        return APIResponse.success(token)
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
