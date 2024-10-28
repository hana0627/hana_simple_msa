package hana.simple.userservice.user.unit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hana.simple.userservice.api.user.controller.UserController
import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserLogin
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.service.UserService
import hana.simple.userservice.global.config.jwt.JwtUtils
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import hana.simple.userservice.user.unit.mock.TestConfig
import hana.simple.userservice.user.unit.mock.TestSecurityConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false) // 테스트코드에서 필터사용 X
@Import(TestSecurityConfig::class, TestConfig::class)
class UserControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var om: ObjectMapper

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @BeforeEach
    fun setUp() {
        val userController: UserController = UserController(userService, jwtUtils)
    }

    @Test
    fun 회원가입이_정상_동작한다() {
        //given
        val userCreate: UserCreate = UserCreate.fixture()

        given(userService.join(userCreate)).willReturn(1L)

        val json = om.writeValueAsString(userCreate)

        // when && then
        mvc.perform(
            post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.OK.name))
            .andExpect(jsonPath("$.result").value(1L))
            .andDo(print())

        then(userService).should().join(userCreate)
    }

    @Test
    fun 아이디_중복이_존재하면_회원가입이_불가능하다() {
        // given
        val userCreate: UserCreate = UserCreate.fixture(userId = "hanana")

        given(userService.join(userCreate)).willThrow(
            ApplicationException(ErrorCode.DUPLICATE_USER, "이미 사용중인 아이디 입니다.")
        )

        val json = om.writeValueAsString(userCreate)

        // when && then
        mvc.perform(post("/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("이미 사용중인 아이디 입니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.CONFLICT.name))
            .andDo(print())
    }


    @Test
    fun 로그인이_성공한다() {
        //given
        val userLogin: UserLogin = UserLogin.fixture(userId = "hanana", password = "rowPassword")
        val userEntity: UserEntity = UserEntity.fixture(userId = "hanana", id=1L)
        val json = om.writeValueAsString(userLogin)

        given(userService.login(userLogin)).willReturn(userEntity)

        //when && then
        mvc.perform(post("/v1/user/login")
            .header("AUTHORIZATION","Bearer safeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("BEARER jwtToken"))
            .andDo(print())

        then(userService).should().login(userLogin)
    }

    @Test
    fun 로그인_실패시_오류를_전달한다() {
        //given
        val userLogin: UserLogin = UserLogin.fixture(userId = "hanana", password = "rowPassword")

        val json = om.writeValueAsString(userLogin)

        given(userService.login(userLogin)).willThrow(
            ApplicationException(ErrorCode.LOGIN_FAIL, "아이디가 없거나 비밀번호가 잘못되었습니다.")
        )

        //when && then
        mvc.perform(post("/v1/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("아이디가 없거나 비밀번호가 잘못되었습니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name))
            .andDo(print())

        then(userService).should().login(userLogin)
    }

    @Test
    @WithMockUser(username = "hanana")
    fun 회원정보_조회가_제대로_이루어진다() {
        // given
        val userId = "hanana"
        val userInformation: UserInformation = UserInformation.fixture(userId = "hanana")
        given(userService.getUserInformation(userId)).willReturn(userInformation)

        // when && then
        mvc.perform(
            get("/v2/user/{userId}", userId)
                .header("AUTHORIZATION","Bearer safeToken"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result.userId").value(userInformation.userId))
            .andExpect(jsonPath("$.result.userName").value(userInformation.userName))
            .andExpect(jsonPath("$.result.phoneNumber").value(userInformation.phoneNumber))
            .andExpect(jsonPath("$.result.gender").value(userInformation.gender.value))
            .andDo(print())

        then(userService).should().getUserInformation(userId)

    }


    @Test
    @WithMockUser(username = "hanana")
    fun 없는_회원정보로_조회시_예외가_발생한다() {
        // given
        val wrongUserId: String = "wrongUser"

        given(userService.getUserInformation(wrongUserId)).willThrow(
            ApplicationException(ErrorCode.USER_NOT_FOUND,  "회원 정보를 찾을 수 없습니다.")
        )

        // when && then
        mvc.perform(get("/v2/user/{userId}",wrongUserId)
            .header("AUTHORIZATION","Bearer safeToken"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value( "회원 정보를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name))
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "hanana")
    fun 패스워드_변경이_정상적으로_이루어진다() {
        // given
        val userId = "hanana"
        val userPasswordChange: UserPasswordChange = UserPasswordChange.fixture()
        given(userService.changePassword(userId, userPasswordChange)).willReturn(1L)

        val json = om.writeValueAsString(userPasswordChange)

        // when && then
        mvc.perform(
            patch("/v2/user/{userId}/password", userId)
                .header("AUTHORIZATION","Bearer safeToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value(1L))
            .andDo(print())

        then(userService).should().changePassword(userId, userPasswordChange)
    }

    @Test
    @WithMockUser(username = "hanana")
    fun 없는_유저정보로_패스워드_변경을_시도하면_예외가_발생한다() {
        // given
        val userId:String = "wrongUser"
        val userPasswordChange: UserPasswordChange = UserPasswordChange.fixture()

        given(userService.changePassword(userId, userPasswordChange)).willThrow(
            ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        )

        val json = om.writeValueAsString(userPasswordChange)

        // when && then
        mvc.perform(patch("/v2/user/{userId}/password", userId)
            .header("AUTHORIZATION","Bearer safeToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("회원 정보를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name))
            .andDo(print())
    }

    @Test
    @WithMockUser(username = "hanana")
    fun 유저_삭제가_정상적으로_이루어진다() {
        //given
        val userId: String = "hanana"

        given(userService.deleteUser(userId)).willReturn(1L)


        // when && then
        mvc.perform(
            delete("/v2/user/{userId}", userId)
                .header("AUTHORIZATION","Bearer safeToken")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value(1L))
            .andDo(print())

        then(userService).should().deleteUser(userId)
    }



    @Test
    @WithMockUser(username = "hanana")
    fun 없는_유저에_대한_삭제를_시도하면_예외가_발생한다() {
        // given
        val userId:String = "wrongUser"

        given(userService.deleteUser(userId)).willThrow(
            ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        )

        // when && then
        mvc.perform(delete("/v2/user/{userId}", userId)
            .header("AUTHORIZATION","Bearer safeToken")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("회원 정보를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name))
            .andDo(print())
    }

}
