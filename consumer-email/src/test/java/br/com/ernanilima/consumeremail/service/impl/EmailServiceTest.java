package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.shared.dto.EmailDTO;
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
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailServiceImpl emailServiceMock;

    @Mock
    private JavaMailSender emailSenderMock;
    @Mock
    private Appender<ILoggingEvent> appenderMock;
    @Captor
    private ArgumentCaptor<ILoggingEvent> argumentCaptor;

    private final String senderSmtpNoReply = "noreply@email.com";
    private final String recipient = "support@email.com";

    @BeforeEach
    void setup() {
        emailServiceMock = new EmailServiceImpl(emailSenderMock);
        setField(emailServiceMock, "senderSmtpNoReply", senderSmtpNoReply);
        setField(emailServiceMock, "recipient", recipient);

        Logger logger = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class.getName());
        logger.addAppender(appenderMock);
    }

    @Test
    @DisplayName("Deve enviar um e-mail")
    void send_Must_Send_An_Email() {
        EmailDTO dto = EmailDTO.builder().sender("email.ok@email.com").message("Mensagem OK").build();

        emailServiceMock.send(dto);

        verify(emailSenderMock, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(emailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}, iniciando envio do e-mail de '{}' com a mensagem '{}'"));
        assertThat(logger0.getLevel(), is(Level.INFO));
        assertThat(logger0.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl, iniciando envio do e-mail de 'email.ok@email.com' com a mensagem 'Mensagem OK'"));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}, e-mail de '{}' foi enviado"));
        assertThat(logger1.getLevel(), is(Level.INFO));
        assertThat(logger1.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl, e-mail de 'email.ok@email.com' foi enviado"));
    }

    @Test
    @DisplayName("Deve retornar um erro por nao enviar o e-mail")
    void send_Must_Return_An_Error_For_Not_Sending_The_Email() {
        EmailDTO dto = EmailDTO.builder().sender("email.ok@email.com").message("Mensagem OK").build();

        MailSendException mailSendException = new MailSendException("Erro retornado");
        doThrow(mailSendException).when(emailSenderMock).send(any(SimpleMailMessage.class));

        emailServiceMock.send(dto);

        verify(emailSenderMock, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(emailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}, iniciando envio do e-mail de '{}' com a mensagem '{}'"));
        assertThat(logger0.getLevel(), is(Level.INFO));
        assertThat(logger0.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl, iniciando envio do e-mail de 'email.ok@email.com' com a mensagem 'Mensagem OK'"));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}, erro ao enviar o e-mail de '{}', MailException '{}'"));
        assertThat(logger1.getLevel(), is(Level.ERROR));
        assertThat(logger1.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl, erro ao enviar o e-mail de 'email.ok@email.com', MailException 'Erro retornado'"));
    }

    @Test
    @DisplayName("Deve retornar os dados do e-mail")
    void prepareSimpleMailMessage_Must_Return_Email_Data() {
        EmailDTO dto = EmailDTO.builder().sender("email.ok@email.com").message("Mensagem OK").build();

        SimpleMailMessage result = emailServiceMock.prepareSimpleMailMessage(dto);

        assertNotNull(result);

        assertThat(result.getFrom(), is(senderSmtpNoReply));
        assertThat(Objects.requireNonNull(result.getTo())[0], is(recipient));
        assertThat(result.getSubject(), is(format("Sugestão de {0}", dto.getSender())));
        assertThat(result.getText(), is(dto.getMessage()));
    }
}