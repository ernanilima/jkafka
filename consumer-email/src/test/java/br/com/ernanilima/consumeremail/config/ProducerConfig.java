package br.com.ernanilima.consumeremail.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@TestConfiguration
public class ProducerConfig implements WebMvcConfigurer {

    @Value("${kafka.topic.email.to.support}")
    private String topicEmailToSupport;

    private final KafkaProperties kafkaProperties;

    @Autowired
    public ProducerConfig(KafkaProperties kafkaProperties) {
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

    @Bean
    public NewTopic topicEmailToSupport() {
        return new NewTopic(topicEmailToSupport, 1, (short) 1);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();

        configs.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

        return new KafkaAdmin(configs);
    }
}
