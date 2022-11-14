package br.com.ernanilima.producer.service.impl;

import br.com.ernanilima.producer.dto.EmailDto;
import br.com.ernanilima.producer.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    @Value("${kafka.topic.email.to.support}")
    private String topicEmailToSupport;

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    @Autowired
    public EmailServiceImpl(KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(EmailDto emailDto) {
        log.info("{}, e-mail de '{}' com a mensagem '{}'",
                this.getClass().getSimpleName(), emailDto.getEmailFrom(), emailDto.getMessage());

        kafkaTemplate.send(topicEmailToSupport, emailDto);
    }
}