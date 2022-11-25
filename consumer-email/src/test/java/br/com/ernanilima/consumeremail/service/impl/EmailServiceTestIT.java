package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.consumeremail.ConsumerEmailTestIT;
import br.com.ernanilima.shared.dto.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;

import java.io.Serializable;

@DirtiesContext
class EmailServiceTestIT extends ConsumerEmailTestIT {

    @InjectMocks
    private EmailServiceImpl consumerServiceMock;

    @MockBean
    private JavaMailSender emailSenderMock;

    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplateMock;

    @BeforeEach
    void setup() {
        consumerServiceMock = new EmailServiceImpl(emailSenderMock);
    }

    @Test
    @DisplayName("Deve enviar um e-mail")
    void send_Must_Send_An_Email() {
        EmailDTO dto = EmailDTO.builder().sender("email.ok@email.com").message("Mensagem OK").build();

        kafkaTemplateMock.send("EMAIL_SEND_SUPPORT", dto);
    }
}