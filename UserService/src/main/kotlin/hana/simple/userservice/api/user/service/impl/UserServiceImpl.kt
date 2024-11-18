package hana.simple.userservice.api.user.service.impl

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserLogin
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.repository.UserCacheRepository
import hana.simple.userservice.api.user.repository.UserRepository
import hana.simple.userservice.api.user.service.UserService
import hana.simple.userservice.global.config.CustomPasswordEncoder
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userCacheRepository: UserCacheRepository,
    private val passwordEncoder: CustomPasswordEncoder,
) : UserService {

    //CREATE
    @Transactional
    override fun join(userCreate: UserCreate): Long {
        return if (isNotDuplicate(userCreate.userId)) {
            val user = UserEntity(
                userId = userCreate.userId,
                userName = userCreate.userName,
                password = passwordEncoder.encode(userCreate.password),
                phoneNumber = userCreate.phoneNumber,
                gender = userCreate.gender,
                boardCount = 0,
                id = null,
            )
            userCacheRepository.setUser(user)
            userRepository.save(user).id!!
        } else {
            throw ApplicationException(ErrorCode.DUPLICATE_USER, "이미 사용중인 아이디 입니다.")
        }
    }

    override fun login(userLogin: UserLogin): UserEntity {
        val user: UserEntity = findUserByUserId(userLogin.userId) ?: throw ApplicationException(
            ErrorCode.LOGIN_FAIL,
            "아이디가 없거나 비밀번호가 잘못되었습니다."
        )
        return if (passwordEncoder.matches(userLogin.password, user.password)) {
            userCacheRepository.setUser(user)
            user
        } else {
            throw ApplicationException(ErrorCode.LOGIN_FAIL, "아이디가 없거나 비밀번호가 잘못되었습니다.")
        }
    }

    //READ
    override fun getUserInformation(userId: String): UserInformation {
        val user: UserEntity =
            findUserByUserId(userId) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        return UserInformation(
            userId = user.userId,
            userName = user.userName,
            phoneNumber = user.phoneNumber,
            gender = user.gender,
        )
    }

    //UPDATE
    @Transactional
    override fun changePassword(userId: String, userPasswordChange: UserPasswordChange): Long {
        val user: UserEntity =
            findUserByUserId(userId) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")

        if (userPasswordChange.newPassword == userPasswordChange.confirmPassword) {
            if (passwordEncoder.matches(userPasswordChange.currentPassword, user.password)) {
                user.changePassword(passwordEncoder.encode(userPasswordChange.newPassword))
                return userRepository.save(user).id!!
            }
        }
        throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "예상하지 못한 오류가 발생했습니다.")
    }

    //DELETE
    @Transactional
    override fun deleteUser(userId: String): Long {
        val user: UserEntity =
            findUserByUserId(userId) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
        userRepository.delete(user)
        return user.id!!
    }


    private fun isNotDuplicate(userId: String): Boolean {
        return userRepository.findByUserId(userId) == null
    }

    private fun findUserByUserId(userId: String): UserEntity? {
        return userCacheRepository.findByUserId(userId) ?: userRepository.findByUserId(userId)
    }

}
