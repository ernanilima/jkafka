package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import org.springframework.kafka.annotation.KafkaListener;

public interface EmailService {
    @KafkaListener(topics = "${kafka.topic.email_to_support}", groupId = "EmailService",
            containerFactory = "containerFactoryEmailToSupport")
    void sendEmailToSupport(EmailToSupportDTO dto);

    @KafkaListener(topics = "${kafka.topic.email_for_verification}", groupId = "EmailService",
            containerFactory = "containerFactoryEmailForVerification")
    void sendEmailForVerification(EmailForVerificationDTO dto);
}
