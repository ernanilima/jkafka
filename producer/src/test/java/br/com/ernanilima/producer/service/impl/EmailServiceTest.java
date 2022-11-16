package br.com.ernanilima.producer.service.impl;

import br.com.ernanilima.producer.builder.EmailDTOBuilder;
import br.com.ernanilima.producer.dto.EmailDto;
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
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
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

        Logger logger = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class.getName());
        logger.addAppender(appenderMock);
    }

    @Test
    @DisplayName("Deve enviar um e-mail")
    void send_Must_Send_An_Email() {
        EmailDto dto = EmailDTOBuilder.create();

        ListenableFuture<SendResult<String, Serializable>> future = new SettableListenableFuture<>();

        when(kafkaTemplateMock.send(anyString(), any(EmailDto.class))).thenReturn(future);

        emailServiceMock.send(dto);

        verify(kafkaTemplateMock, times(1)).send(any(), any());
        verifyNoMoreInteractions(kafkaTemplateMock);

        verify(appenderMock, times(1)).doAppend(argumentCaptor.capture());
        ILoggingEvent loggingEvent = argumentCaptor.getAllValues().get(0);
        assertThat(loggingEvent.getMessage(), is("{}, e-mail de '{}' com a mensagem '{}'"));
        assertThat(loggingEvent.getLevel(), is(Level.INFO));
    }
}