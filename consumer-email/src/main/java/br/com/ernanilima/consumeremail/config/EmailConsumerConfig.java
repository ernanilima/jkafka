package br.com.ernanilima.consumeremail.config;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
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

import java.io.Serializable;
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
    public <T extends Serializable> JsonDeserializer<T> neutarUserKafkaJsonDeserializer() {
        JsonDeserializer<T> json = new JsonDeserializer<>();
        json.addTrustedPackages("br.com.ernanilima.*");
        return json;
    }

    @Bean
    public <T> ConsumerFactory<String, T> consumerFactory(JsonDeserializer<T> deserializer) {
        Map<String, Object> properties = new HashMap<>();

        properties.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
        properties.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailToSupportDTO> containerFactoryEmailToSupport(
            ConsumerFactory<String, EmailToSupportDTO> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailToSupportDTO>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailForVerificationDTO> containerFactoryEmailForVerification(
            ConsumerFactory<String, EmailForVerificationDTO> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, EmailForVerificationDTO>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
