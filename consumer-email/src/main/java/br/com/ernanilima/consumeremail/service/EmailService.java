package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.consumeremail.dto.EmailDTO;
import org.springframework.kafka.annotation.KafkaListener;

public interface EmailService {
    @KafkaListener(topics = "${kafka.topic.email.to.support}", groupId = "EmailServiceImpl",
            containerFactory = "concurrentKafkaListenerContainerFactory")
    void send(EmailDTO dto);
}
