package hana.simple.userservice.user.unit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hana.simple.userservice.api.user.controller.UserController
import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.service.UserService
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@MockBean(JpaMetamodelMappingContext::class) // JPA 불러오기
@WebMvcTest(UserController::class)
class UserControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var om: ObjectMapper

    @MockBean
    private lateinit var userService: UserService

    fun setUp() {
        val userController = UserController(userService)
    }


    @Test
    fun 회원가입이_정상_동작한다() {
        //given
        val userCreate: UserCreate = UserCreate.fixture()

        given(userService.join(userCreate)).willReturn(1L)

        val json = om.writeValueAsString(userCreate)

        // when && then
        mvc.perform(
            post("/v1/create/user")
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
        mvc.perform(post("/v1/create/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("이미 사용중인 아이디 입니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.CONFLICT.name))
            .andDo(print())

    }


    @Test
    fun 회원정보_조회가_제대로_이루어진다() {
        // given
        val userId = "hanana"
        val userInformation: UserInformation = UserInformation.fixture(userId = "hanana")
        given(userService.getUserInformation(userId)).willReturn(userInformation)

        // when && then
        mvc.perform(
            get("/v2/user/{userId}", userId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result.userId").value(userInformation.userId))
            .andExpect(jsonPath("$.result.userName").value(userInformation.userName))
            .andExpect(jsonPath("$.result.phoneNumber").value(userInformation.phoneNumber))
            .andExpect(jsonPath("$.result.gender").value(userInformation.gender.value))
            .andDo(print())

        then(userService).should().getUserInformation(userId)

    }


    @Test
    fun 없는_회원정보로_조회시_예외가_발생한다() {
        // given
        val userCreate: UserCreate = UserCreate.fixture(userId = "hanana")

        given(userService.join(userCreate)).willThrow(
            ApplicationException(ErrorCode.DUPLICATE_USER, "이미 사용중인 아이디 입니다.")
        )

        val json = om.writeValueAsString(userCreate)

        // when && then
        mvc.perform(post("/v1/create/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("이미 사용중인 아이디 입니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.CONFLICT.name))
            .andDo(print())
    }

    @Test
    fun 패스워드_변경이_정상적으로_이루어진다() {
        // given
        val userId = "hanana"
        val userPasswordChange: UserPasswordChange = UserPasswordChange.fixture()
        given(userService.changePassword(userId, userPasswordChange)).willReturn(1L)

        val json = om.writeValueAsString(userPasswordChange)

        // when && then
        mvc.perform(
            patch("/v2/user/{userId}/password", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value(1L))
            .andDo(print())

        then(userService).should().changePassword(userId, userPasswordChange)
    }

    @Test
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
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("회원 정보를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name))
            .andDo(print())
    }

    @Test
    fun 유저_삭제가_정상적으로_이루어진다() {
        //given
        val userId: String = "hanana"

        given(userService.deleteUser(userId)).willReturn(1L)


        // when && then
        mvc.perform(
            delete("/v2/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value(1L))
            .andDo(print())

        then(userService).should().deleteUser(userId)
    }



    @Test
    fun 없는_유저에_대한_삭제를_시도하면_예외가_발생한다() {
        // given
        val userId:String = "wrongUser"

        given(userService.deleteUser(userId)).willThrow(
            ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        )

        // when && then
        mvc.perform(delete("/v2/user/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.result").value("회원 정보를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.resultCode").value(HttpStatus.NOT_FOUND.name))
            .andDo(print())
    }

}
