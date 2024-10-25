package hana.simple.userservice.api.user.repository

import hana.simple.userservice.api.user.controller.response.UserInformation
import hana.simple.userservice.api.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUserId(userId: String): UserEntity?
}
