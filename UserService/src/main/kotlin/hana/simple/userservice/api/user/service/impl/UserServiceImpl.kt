package hana.simple.userservice.api.user.service.impl

import hana.simple.userservice.api.common.domain.constant.Gender
import hana.simple.userservice.api.user.controller.request.UserCreate
import hana.simple.userservice.api.user.controller.request.UserPasswordChange
import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.repository.UserRepository
import hana.simple.userservice.api.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService {
    //TODO 예외처리 추가
    //TODO redis 추가
    //TODO passwordEncoder 추가
    //TODO findById 메서드 분리
    
    //CREATE
    @Transactional
    override fun join(dto: UserCreate): Long {
        if(duplicateUser(dto.userId)) {
            val user: UserEntity = UserEntity(
                userId = dto.userId,
                userName = dto.userName,
                password = dto.password,
                phoneNumber = dto.phoneNumber,
                gender = dto.gender,
                id = null,
            )
            return userRepository.save(user).id!!
        }
        throw RuntimeException()
    }

    //READ
    override fun getUserInformation(userId: String): UserInformation {
        val user: UserEntity = userRepository.findByUserId(userId) ?: throw RuntimeException()
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
        val user: UserEntity = userRepository.findByUserId(userId) ?: throw RuntimeException()
        if(user.password.equals(userPasswordChange.currentPassword) && userPasswordChange.newPassword == userPasswordChange.confirmPassword) {
            user.changePassword(userPasswordChange.newPassword);
            return userRepository.save(user).id!!

        }
        throw RuntimeException()
    }

    //DELETE
    @Transactional
    override fun deleteUser(userId: String): Long {
        val user: UserEntity = userRepository.findByUserId(userId) ?: throw RuntimeException()
        userRepository.delete(user)
        return user.id!!
    }


    private fun duplicateUser(userId: String): Boolean {
        if(userRepository.findByUserId(userId) != null) {
            throw RuntimeException()
        }
        return true
    }

}
