package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import org.springframework.kafka.annotation.KafkaListener;

public interface EmailService {
    @KafkaListener(topics = "${kafka.topic.email.to.support}", groupId = "EmailService",
            containerFactory = "concurrentKafkaListenerContainerFactory")
    void sendEmailToSupport(EmailToSupportDTO dto);
}
