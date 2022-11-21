package br.com.ernanilima.producer;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.utility.DockerImageName.parse;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = {ProducerApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class ProducerTestIT {

    @Container
    public static KafkaContainer kafka = new KafkaContainer(parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}

