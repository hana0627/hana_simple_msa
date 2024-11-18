package hana.simple.userservice.api.message

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import hana.simple.userservice.api.user.domain.UserEntity
import hana.simple.userservice.api.user.repository.UserRepository
import hana.simple.userservice.global.exception.ApplicationException
import hana.simple.userservice.global.exception.constant.ErrorCode
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@RequiredArgsConstructor
@Transactional
class KafkaConsumer @Autowired constructor(
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
){
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["hana_sample_create"], groupId = "hana_group_id")
    fun plusBoardCount(message: String) {
        println("KafkaConsumer.plusBoardCount")
        log.info("kafka message -> $message")

        val map: Map<String, Any> = objectMapper.readValue(message, object : TypeReference<Map<String, Any>>() {})
        val userId: String = map["userId"] as? String ?: throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR)

        val user: UserEntity = userRepository.findByUserId(userId) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.");

        user.plusBoardCount()
        //TODO 카프카 커넥터 사용
        //TODO 예외상황 발생시 롤백이벤트 발행 (어캐함??)
    }
    @KafkaListener(topics = ["hana_sample_delete"], groupId = "hana_group_id")
    fun minusBoardCount(message: String) {
        println("KafkaConsumer.minusBoardCount")
        log.info("kafka message -> $message")

        val map: Map<String, Any> = objectMapper.readValue(message, object : TypeReference<Map<String, Any>>() {})
        val userId: String = map["userId"] as? String ?: throw ApplicationException(ErrorCode.INTERNAL_SERVER_ERROR)

        val user: UserEntity = userRepository.findByUserId(userId) ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.");

        user.minusBoardCount()

    }

}