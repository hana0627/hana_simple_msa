package hana.simple.boardservice.api.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(String topic, String key, String message) {
        try {
            SendResult<String, String> result = kafkaTemplate.send(topic, key, message).get();
            return result.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }

}

