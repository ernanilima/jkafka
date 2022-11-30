package br.com.ernanilima.producer.service.impl;

import br.com.ernanilima.producer.service.EmailService;
import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    private final String CLASS_NAME = "producer." + this.getClass().getSimpleName();

    @Value("${kafka.topic.email_to_support}")
    private String topicEmailToSupport;
    @Value("${kafka.topic.email_for_verification}")
    private String topicEmailForVerification;

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    @Autowired
    public EmailServiceImpl(KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendEmailToSupport(EmailToSupportDTO dto) {
        log.info("{}:sendEmailToSupport(obj), e-mail de '{}' com a mensagem '{}'",
                CLASS_NAME, dto.getSender(), dto.getMessage());

        kafkaTemplate.send(topicEmailToSupport, dto);
    }

    @Override
    public void sendEmailForVerification(EmailForVerificationDTO dto) {
        log.info("{}:sendEmailForVerification(obj), e-mail para '{}'",
                CLASS_NAME, dto.getEmailForVerification());

        kafkaTemplate.send(topicEmailForVerification, dto);
    }
}