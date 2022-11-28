package br.com.ernanilima.consumeremail.config;

import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
public class EmailConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Autowired
    public EmailConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, EmailToSupportDTO> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<EmailToSupportDTO> deserializer = new JsonDeserializer<>(EmailToSupportDTO.class)
                .trustedPackages("br.com.ernanilima.*").forKeys();

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailToSupportDTO> concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EmailToSupportDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
