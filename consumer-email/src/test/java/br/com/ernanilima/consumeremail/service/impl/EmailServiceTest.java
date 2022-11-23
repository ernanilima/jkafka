package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.shared.dto.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailServiceImpl emailServiceMock;

    @Mock
    private JavaMailSender emailSenderMock;

    @BeforeEach
    void setup() {
        emailServiceMock = new EmailServiceImpl(emailSenderMock);
        setField(emailServiceMock, "senderSmtpNoReply", "noreply@email.com");
        setField(emailServiceMock, "recipient", "support@email.com");
    }

    @Test
    @DisplayName("Deve enviar um e-mail")
    void send_Must_Send_An_Email() {
        EmailDTO dto = EmailDTO.builder().sender("email.ok@email.com").message("Mensagem OK").build();

        emailServiceMock.send(dto);

        verify(emailSenderMock, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(emailSenderMock);
    }
}