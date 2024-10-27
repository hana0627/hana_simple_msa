package hana.simple.userservice.user.unit.service

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.repository.UserRepository
import hana.simple.userservice.api.user.service.UserService
import hana.simple.userservice.api.user.service.impl.UserServiceImpl
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserServiceTest (
    @Mock private val userRepository: UserRepository,
){

    @InjectMocks
    private val userService: UserService = UserServiceImpl(userRepository)


    @Test
    fun 회원가입이_정상_동작한다() {
        //given
        val dto: UserCreate = UserCreate.fixture()
        val userEntity: UserEntity = UserEntity.fixture()

        given(userRepository.findByUserId(dto.userId)).willReturn(null)
        given(userRepository.save(userEntity)).willReturn(UserEntity.fixture(id = 1L))

        //when
        val result: Long = userService.join(dto)

        //then
        then(userRepository).should().findByUserId(dto.userId)
        then(userRepository).should().save(userEntity)

        assertThat(result).isEqualTo(1L)
    }

    @Test
    fun 아이디_중복이_존재하면_회원가입이_불가능하다() {
        //given
        val dto: UserCreate = UserCreate.fixture(userId = "hanana")
        val existingUser: UserEntity = UserEntity.fixture(userId = "hanana")

        given(userRepository.findByUserId(dto.userId)).willReturn(existingUser)

        //when & then
        val result = assertThrows<ApplicationException> {userService.join(dto)}

        then(userRepository).should().findByUserId(dto.userId)
        then(userRepository).should(times(0)).save(existingUser)

//        assertThat(result.errorCode).isEqualTo(ErrorCode.DUPLICATE_USER)
        assertThat(result.message).isEqualTo("이미 사용중인 아이디 입니다.")
        assertThat(result.getErrorCode).isEqualTo(ErrorCode.DUPLICATE_USER)
        assertThat(result.getMessage ).isEqualTo("이미 사용중인 아이디 입니다.")
    }

    @Test
    fun 회원정보_조회가_제대로_이루어진다() {
        //given
        val dto: UserInformation = UserInformation.fixture()
        val userEntity: UserEntity = UserEntity.fixture(userId = "hanana")

        given(userRepository.findByUserId(dto.userId)).willReturn(userEntity)

        //when
        val result: UserInformation = userService.getUserInformation(dto.userId)

        //then
        then(userRepository).should().findByUserId(dto.userId)

        assertThat(result.userId).isEqualTo(userEntity.userId)
        assertThat(result.userName).isEqualTo(userEntity.userName)
        assertThat(result.phoneNumber).isEqualTo(userEntity.phoneNumber)
        assertThat(result.gender).isEqualTo(userEntity.gender)
    }

    @Test
    fun 없는_회원정보로_조회시_예외가_발생한다() {
        //given
        val dto: UserInformation = UserInformation.fixture()
        val userEntity: UserEntity = UserEntity.fixture(userId = "hanana")

        given(userRepository.findByUserId("wrongUser")).willReturn(null)

        //when
        val result = assertThrows<ApplicationException> {userService.getUserInformation("wrongUser")}

        //then
        then(userRepository).should().findByUserId("wrongUser")

//        assertThat(result.errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
        assertThat(result.message).isEqualTo("회원 정보를 찾을 수 없습니다.")
        assertThat(result.getErrorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
        assertThat(result.getMessage ).isEqualTo("회원 정보를 찾을 수 없습니다.")
    }

    @Test
    fun 패스워드_변경이_정상적으로_이루어진다() {
        //given
        val userId = "hanana"
        val userPasswordChange: UserPasswordChange = UserPasswordChange.fixture(currentPassword = "password", userId = "hanana")
        val userEntity: UserEntity = UserEntity.fixture(id = 1L, userId = "hanana", password = "password")

        given(userRepository.findByUserId(userId)).willReturn(userEntity)
        given(userRepository.save(userEntity)).willReturn(userEntity)

        //when
        val result: Long = userService.changePassword(userId, userPasswordChange)

        //then
        then(userRepository).should().findByUserId(userId)
        then(userRepository).should().save(userEntity)

        assertThat(userEntity.password).isEqualTo(userPasswordChange.newPassword)
    }

    @Test
    fun 없는_유저정보로_패스워드_변경을_시도하면_예외가_발생한다() {
        //given
        val userId = "hanana"
        val userPasswordChange: UserPasswordChange = UserPasswordChange.fixture(userId = "hanana")
        val userEntity: UserEntity = UserEntity.fixture(userId = "hanana", password = "password")

        given(userRepository.findByUserId("wrongUser")).willReturn(null)

        //when & then
        val result = assertThrows<ApplicationException> { userService.changePassword(userId, userPasswordChange)}
        then(userRepository).should(times(0)).save(any())

//        assertThat(result.errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
        assertThat(result.message).isEqualTo("회원 정보를 찾을 수 없습니다.")
        assertThat(result.getErrorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
        assertThat(result.getMessage ).isEqualTo("회원 정보를 찾을 수 없습니다.")
    }


    @Test
    fun 유저_삭제가_정상적으로_이루어진다() {
        //given
        val userId: String = "hanana"
        val userEntity: UserEntity = UserEntity.fixture(id = 1L, userId = "hanana")

        given(userRepository.findByUserId(userId)).willReturn(userEntity)

        //when
        val result: Long = userService.deleteUser(userId)

        //then
        then(userRepository).should().findByUserId(userId)
        then(userRepository).should().delete(userEntity)

        assertThat(result).isEqualTo(userEntity.id)
    }

    @Test
    fun 없는_유저에_대한_삭제를_시도하면_예외가_발생한다() {
        //given
        val userId: String = "wrongUser"
        val userEntity: UserEntity = UserEntity.fixture(id = 1L, userId = "hanana")

        given(userRepository.findByUserId(userId)).willReturn(null)

        //when & then
        val result = assertThrows<ApplicationException> { userService.deleteUser(userId) }

        then(userRepository).should().findByUserId(userId)
        then(userRepository).should(times(0)).delete(any())
        
        assertThat(result.message).isEqualTo("회원 정보를 찾을 수 없습니다.")
        assertThat(result.getErrorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
        assertThat(result.getMessage ).isEqualTo("회원 정보를 찾을 수 없습니다.")
    }
}
