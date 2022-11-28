package br.com.ernanilima.producer.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig implements WebMvcConfigurer {

    private final KafkaProperties kafkaProperties;

    @Autowired
    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, Serializable> producerFactory() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, Serializable> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
