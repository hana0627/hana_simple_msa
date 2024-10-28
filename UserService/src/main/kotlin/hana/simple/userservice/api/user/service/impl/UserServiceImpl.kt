package hana.simple.userservice.api.user.service.impl

import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserLogin
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
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
    private val passwordEncoder: CustomPasswordEncoder,
) : UserService {
    //TODO redis 추가

    //CREATE
    @Transactional
    override fun join(dto: UserCreate): Long {
        if(duplicateUser(dto.userId)) {
            val user: UserEntity = UserEntity(
                userId = dto.userId,
                userName = dto.userName,
                password = passwordEncoder.encode(dto.password),
                phoneNumber = dto.phoneNumber,
                gender = dto.gender,
                id = null,
            )
            return userRepository.save(user).id!!
        }
        throw RuntimeException()
    }

    override fun login(userLogin: UserLogin): Long {
        val user: UserEntity = getUserByUserIdOrException(userLogin.userId)
        return if (passwordEncoder.matches(userLogin.password, user.password)) {
            user.id!!
        }else {
            throw ApplicationException(ErrorCode.LOGIN_FAIL, "아이디가 없거나 비밀번호가 잘못되었습니다.")
        }
    }

    //READ
    override fun getUserInformation(userId: String): UserInformation {
        val user: UserEntity = getUserByUserIdOrException(userId)
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
        val user: UserEntity = getUserByUserIdOrException(userId)

        if(userPasswordChange.newPassword == userPasswordChange.confirmPassword) {
            if(passwordEncoder.matches(userPasswordChange.currentPassword, user.password)) {
                user.changePassword(passwordEncoder.encode(userPasswordChange.newPassword))
                return userRepository.save(user).id!!
            }
        }
        throw RuntimeException()
    }

    //DELETE
    @Transactional
    override fun deleteUser(userId: String): Long {
        val user: UserEntity = getUserByUserIdOrException(userId)
        userRepository.delete(user)
        return user.id!!
    }


    private fun duplicateUser(userId: String): Boolean {
        if(userRepository.findByUserId(userId) != null) {
            throw ApplicationException(ErrorCode.DUPLICATE_USER, "이미 사용중인 아이디 입니다.")
        }
        return true
    }

    private fun getUserByUserIdOrException(userId: String): UserEntity {
        return userRepository.findByUserId(userId) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.")
    }

}
