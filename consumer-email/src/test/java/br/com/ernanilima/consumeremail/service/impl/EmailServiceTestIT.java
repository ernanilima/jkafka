package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.consumeremail.ConsumerEmailTestIT;
import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import br.com.ernanilima.shared.test.builder.EmailForVerificationBuilder;
import br.com.ernanilima.shared.test.builder.EmailToSupportBuilder;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DirtiesContext
class EmailServiceTestIT extends ConsumerEmailTestIT {

    @InjectMocks
    private EmailServiceImpl consumerServiceMock;

    @MockBean
    private JavaMailSender mailSenderMock;

    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplateMock;

    @MockBean
    private Appender<ILoggingEvent> appenderMock;

    @Captor
    private ArgumentCaptor<ILoggingEvent> argumentCaptor;

    @BeforeEach
    void setup() {
        consumerServiceMock = new EmailServiceImpl(mailSenderMock);

        Logger logger = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class.getName());
        logger.addAppender(appenderMock);
    }

    @Test
    @DisplayName("Deve enviar um e-mail (sendEmailToSupport)")
    void sendEmailToSupport_Must_Send_An_Email() throws InterruptedException {
        EmailToSupportDTO dto = EmailToSupportBuilder.create();

        kafkaTemplateMock.send("EMAIL_SEND_SUPPORT", dto);

        TimeUnit.SECONDS.sleep(1);

        verify(mailSenderMock, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(mailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}:sendEmailToSupport(obj), iniciando envio do e-mail de '{}' com a mensagem '{}'"));
        assertThat(logger0.getArgumentArray()[0].toString(), containsString(consumerServiceMock.getClass().getSimpleName()));
        assertThat(logger0.getArgumentArray()[1], hasToString(dto.getSender()));
        assertThat(logger0.getArgumentArray()[2], hasToString(dto.getMessage()));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}:sendEmailToSupport(obj), e-mail de '{}' foi enviado"));
        assertThat(logger1.getArgumentArray()[0].toString(), containsString(consumerServiceMock.getClass().getSimpleName()));
        assertThat(logger1.getArgumentArray()[1], hasToString(dto.getSender()));
    }

    @Test
    @DisplayName("Deve enviar um e-mail (sendEmailForVerification)")
    void sendEmailForVerification_Must_Send_An_Email() throws InterruptedException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        when(mailSenderMock.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        kafkaTemplateMock.send("EMAIL_SEND_VERIFICATION", dto);

        TimeUnit.SECONDS.sleep(1);

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}:sendEmailForVerification(obj), iniciando envio do e-mail para '{}'"));
        assertThat(logger0.getArgumentArray()[0].toString(), containsString(consumerServiceMock.getClass().getSimpleName()));
        assertThat(logger0.getArgumentArray()[1], hasToString(dto.getEmailForVerification()));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}:sendEmailForVerification(obj), foi enviado o e-mail para '{}'"));
        assertThat(logger1.getArgumentArray()[0].toString(), containsString(consumerServiceMock.getClass().getSimpleName()));
        assertThat(logger1.getArgumentArray()[1], hasToString(dto.getEmailForVerification()));
    }
}