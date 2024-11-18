package hana.simple.userservice.api.user.domain

import hana.simple.userservice.api.common.domain.AuditingFields
import hana.simple.userservice.api.common.domain.constant.Gender
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import jakarta.persistence.*

@Entity
@Table(name = "user_account")
data class UserEntity (
    @Column(length = 100, updatable = false)
    var userId: String,
    @Column(length = 50)
    var userName: String,
    @Column(length = 200)
    var password: String,
    @Column(length = 50)
    var phoneNumber: String,
    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    var gender: Gender,
    var boardCount: Int,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
) : AuditingFields() {
    fun changePassword(newPassword: String) {
        this.password = newPassword
    }

    companion object{
        fun fixture(
            userId: String = "hanana0627",
            userName: String = "박하나",
            password: String = "password1234",
            phoneNumber: String = "01012345678",
            gender: Gender = Gender.F,
            boardCount: Int? =  0,
            id: Long? = null
        ) : UserEntity {
            return UserEntity(
                userId = userId,
                userName = userName,
                password = password,
                phoneNumber = phoneNumber,
                gender = gender,
                boardCount = boardCount?:0,
                id = id
            )
        }
    }


    fun plusBoardCount() {
        if(boardCount > 10) {
            throw ApplicationException(ErrorCode.MAX_BOARD_COUNT,"게시글은 10개까지 작성 가능합니다.")
        }
        boardCount++
    }

    fun minusBoardCount() {
        boardCount--
    }
}
