package hana.simple.userservice.api.user.service.impl

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
}
