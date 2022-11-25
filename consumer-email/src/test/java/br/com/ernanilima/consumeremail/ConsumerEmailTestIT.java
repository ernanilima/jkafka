package br.com.ernanilima.consumeremail;

import br.com.ernanilima.consumeremail.config.ProducerConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.utility.DockerImageName.parse;

@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {ProducerConfig.class})
@SpringBootTest(classes = {ConsumerEmailApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class ConsumerEmailTestIT {

    @Container
    public static KafkaContainer kafka = new KafkaContainer(parse("confluentinc/cp-kafka:latest"));

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("default.sender.smtp.noreply", () -> "noreply@email.com");
        registry.add("default.password.smtp.noreply", () -> "0918273645");
        registry.add("default.recipient", () -> "support@email.com");
    }
}
