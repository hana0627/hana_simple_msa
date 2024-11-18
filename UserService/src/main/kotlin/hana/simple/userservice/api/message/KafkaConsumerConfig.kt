package hana.simple.userservice.api.message

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@EnableKafka
class KafkaConsumerConfig {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun consumerFactory() : ConsumerFactory<String, String> {
        val properties = HashMap<String, Any>()
        properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "kafka:9092"
        properties[ConsumerConfig.GROUP_ID_CONFIG] = "hana_group_id"
        properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        return DefaultKafkaConsumerFactory(properties);
    }

    @Bean
    fun kafkaListener(kafkaListenerContainerFactory: ConcurrentKafkaListenerContainerFactory<String, String>) : ConcurrentKafkaListenerContainerFactory<String, String> {
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>();
        kafkaListenerContainerFactory.consumerFactory = consumerFactory()
        return containerFactory;
    }

}