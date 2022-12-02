package br.com.ernanilima.producer.service.impl;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import br.com.ernanilima.shared.test.builder.EmailForVerificationBuilder;
import br.com.ernanilima.shared.test.builder.EmailToSupportBuilder;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailServiceImpl emailServiceMock;

    @Mock
    private KafkaTemplate<String, Serializable> kafkaTemplateMock;
    @Mock
    private Appender<ILoggingEvent> appenderMock;
    @Captor
    private ArgumentCaptor<ILoggingEvent> argumentCaptor;

    @BeforeEach
    void setup() {
        emailServiceMock = new EmailServiceImpl(kafkaTemplateMock);
        setField(emailServiceMock, "topicEmailToSupport", "EMAIL_SEND_SUPPORT");
        setField(emailServiceMock, "topicEmailForVerification", "EMAIL_SEND_VERIFICATION");

        Logger logger = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class.getName());
        logger.addAppender(appenderMock);
    }

    @Test
    @DisplayName("Deve enviar um e-mail (sendEmailToSupport)")
    void sendEmailToSupport_Must_Send_An_Email() {
        EmailToSupportDTO dto = EmailToSupportBuilder.create();

        when(kafkaTemplateMock.send(anyString(), any(EmailToSupportDTO.class))).thenReturn(new SettableListenableFuture<>());

        emailServiceMock.sendEmailToSupport(dto);

        verify(kafkaTemplateMock, times(1)).send(any(), any());
        verifyNoMoreInteractions(kafkaTemplateMock);

        verify(appenderMock, times(1)).doAppend(argumentCaptor.capture());
        ILoggingEvent loggingEvent = argumentCaptor.getAllValues().get(0);
        assertThat(loggingEvent.getMessage(), is("{}:sendEmailToSupport(obj), e-mail de '{}' com a mensagem '{}'"));
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(),
                is("producer.EmailServiceImpl:sendEmailToSupport(obj), e-mail de 'email.ok@email.com' com a mensagem 'Mensagem OK'"));
    }

    @Test
    @DisplayName("Deve enviar um e-mail (sendEmailForVerification)")
    void sendEmailForVerification_Must_Send_An_Email() {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        when(kafkaTemplateMock.send(anyString(), any(EmailForVerificationDTO.class))).thenReturn(new SettableListenableFuture<>());

        emailServiceMock.sendEmailForVerification(dto);

        verify(kafkaTemplateMock, times(1)).send(any(), any());
        verifyNoMoreInteractions(kafkaTemplateMock);

        verify(appenderMock, times(1)).doAppend(argumentCaptor.capture());
        ILoggingEvent loggingEvent = argumentCaptor.getAllValues().get(0);
        assertThat(loggingEvent.getMessage(), is("{}:sendEmailForVerification(obj), e-mail para '{}'"));
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
        assertThat(loggingEvent.getFormattedMessage(),
                is("producer.EmailServiceImpl:sendEmailForVerification(obj), e-mail para 'email.ok@email.com'"));
    }
}