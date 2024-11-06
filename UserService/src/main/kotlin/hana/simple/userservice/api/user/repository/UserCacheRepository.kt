package hana.simple.userservice.api.user.repository

import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.global.TOKEN_CACHE_TTL
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class UserCacheRepository(
    private val redisTemplate: RedisTemplate<String, UserEntity>
) {

    private val log = LoggerFactory.getLogger(javaClass)


    fun setUser(user: UserEntity): Unit{
        val key = "User:${user.userId}"
        log.info("Set User to redis $key, $user")
        redisTemplate.opsForValue().set(key, user, TOKEN_CACHE_TTL)
    }

    fun findByUserId(userId: String): UserEntity? {
        val key = "User:$userId"
        val user: UserEntity? = redisTemplate.opsForValue().get(key)
        log.info("Get User From Redis $key, $user")
        return user
    }
}
