package br.com.ernanilima.producer.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
@ConfigurationProperties(prefix = "kafka.topic")
public final class KafkaTopicPrepare {

    private final List<String> topics = new ArrayList<>();

    private String emailToSupport;
    private String emailForVerification;

    @PostConstruct
    public void postConstruct() {
        topics.addAll(Arrays.asList(emailToSupport, emailForVerification));
    }
}
