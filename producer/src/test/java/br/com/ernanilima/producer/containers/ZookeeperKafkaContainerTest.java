package br.com.ernanilima.producer.containers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.utility.DockerImageName.parse;

@ExtendWith(MockitoExtension.class)
public class ZookeeperKafkaContainerTest {

    private static final DockerImageName ZOOKEEPER = parse("confluentinc/cp-zookeeper:latest");
    private static final DockerImageName KAFKA = parse("confluentinc/cp-kafka:latest");

    @Test
    void runZookeeperImage() {
        try (GenericContainer<?> zookeeper = new GenericContainer<>(ZOOKEEPER)) {
            zookeeper.start();
        }
    }

    @Test
    void runKafkaImage() {
        try (KafkaContainer kafka = new KafkaContainer(KAFKA)) {
            kafka.start();
        }
    }
}
