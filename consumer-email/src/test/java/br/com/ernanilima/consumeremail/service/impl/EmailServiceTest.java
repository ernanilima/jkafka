package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import br.com.ernanilima.shared.test.builder.EmailForVerificationBuilder;
import br.com.ernanilima.shared.test.builder.EmailToSupportBuilder;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import freemarker.template.Configuration;
import freemarker.template.Template;
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

import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailServiceImpl emailServiceMock;

    @Mock
    private JavaMailSender mailSenderMock;
    @Mock
    private Configuration configMock;
    @Mock
    private Appender<ILoggingEvent> appenderMock;
    @Captor
    private ArgumentCaptor<ILoggingEvent> argumentCaptor;

    @BeforeEach
    void setup() {
        emailServiceMock = new EmailServiceImpl(mailSenderMock);
        emailServiceMock.setConfig(configMock);
        setField(emailServiceMock, "senderNoreply", "noreply@email.com");

        Logger logger = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class.getName());
        logger.addAppender(appenderMock);
    }

    @Test
    @DisplayName("Deve enviar um e-mail (sendEmailToSupport)")
    void sendEmailToSupport_Must_Send_An_Email() {
        EmailToSupportDTO dto = EmailToSupportBuilder.create();

        emailServiceMock.sendEmailToSupport(dto);

        verify(mailSenderMock, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(mailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}:sendEmailToSupport(obj), iniciando envio do e-mail de '{}' com a mensagem '{}'"));
        assertThat(logger0.getLevel(), is(Level.INFO));
        assertThat(logger0.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl:sendEmailToSupport(obj), iniciando envio do e-mail de 'email.ok@email.com' com a mensagem 'Mensagem OK'"));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}:sendEmailToSupport(obj), e-mail de '{}' foi enviado"));
        assertThat(logger1.getLevel(), is(Level.INFO));
        assertThat(logger1.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl:sendEmailToSupport(obj), e-mail de 'email.ok@email.com' foi enviado"));
    }

    @Test
    @DisplayName("Deve retornar um erro por nao enviar o e-mail (sendEmailToSupport)")
    void sendEmailToSupport_Must_Return_An_Error_For_Not_Sending_The_Email() {
        EmailToSupportDTO dto = EmailToSupportBuilder.create();

        MailSendException mailSendException = new MailSendException("Erro retornado");
        doThrow(mailSendException).when(mailSenderMock).send(any(SimpleMailMessage.class));

        emailServiceMock.sendEmailToSupport(dto);

        verify(mailSenderMock, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(mailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}:sendEmailToSupport(obj), iniciando envio do e-mail de '{}' com a mensagem '{}'"));
        assertThat(logger0.getLevel(), is(Level.INFO));
        assertThat(logger0.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl:sendEmailToSupport(obj), iniciando envio do e-mail de 'email.ok@email.com' com a mensagem 'Mensagem OK'"));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}:sendEmailToSupport(obj), erro ao enviar o e-mail de '{}', MailException '{}'"));
        assertThat(logger1.getLevel(), is(Level.ERROR));
        assertThat(logger1.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl:sendEmailToSupport(obj), erro ao enviar o e-mail de 'email.ok@email.com', MailException 'Erro retornado'"));
    }

    @Test
    @DisplayName("Deve enviar um e-mail (sendEmailForVerification)")
    void sendEmailForVerification_Must_Send_An_Email() throws IOException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        when(mailSenderMock.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(configMock.getTemplate(anyString())).thenReturn(mock(Template.class));

        emailServiceMock.sendEmailForVerification(dto);

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
        verifyNoMoreInteractions(mailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}:sendEmailForVerification(obj), iniciando envio do e-mail para '{}'"));
        assertThat(logger0.getArgumentArray()[0].toString(), containsString(emailServiceMock.getClass().getSimpleName()));
        assertThat(logger0.getArgumentArray()[1], hasToString(dto.getEmailForVerification()));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}:sendEmailForVerification(obj), foi enviado o e-mail para '{}'"));
        assertThat(logger1.getArgumentArray()[0].toString(), containsString(emailServiceMock.getClass().getSimpleName()));
        assertThat(logger1.getArgumentArray()[1], hasToString(dto.getEmailForVerification()));
    }

    @Test
    @DisplayName("Deve retornar um erro por nao enviar o e-mail (sendEmailForVerification)")
    void sendEmailForVerification_Must_Return_An_Error_For_Not_Sending_The_Email() throws IOException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        MailSendException mailSendException = new MailSendException("Erro retornado");
        doThrow(mailSendException).when(mailSenderMock).send(any(MimeMessage.class));
        when(mailSenderMock.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(configMock.getTemplate(anyString())).thenReturn(mock(Template.class));

        emailServiceMock.sendEmailForVerification(dto);

        verify(mailSenderMock, times(1)).send(any(MimeMessage.class));
        verifyNoMoreInteractions(mailSenderMock);

        verify(appenderMock, times(2)).doAppend(argumentCaptor.capture());

        ILoggingEvent logger0 = argumentCaptor.getAllValues().get(0);
        assertThat(logger0.getMessage(), is("{}:sendEmailForVerification(obj), iniciando envio do e-mail para '{}'"));
        assertThat(logger0.getLevel(), is(Level.INFO));
        assertThat(logger0.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl:sendEmailForVerification(obj), iniciando envio do e-mail para 'email.ok@email.com'"));

        ILoggingEvent logger1 = argumentCaptor.getAllValues().get(1);
        assertThat(logger1.getMessage(), is("{}:sendEmailForVerification(obj), erro ao enviar o e-mail para '{}', MailException '{}'"));
        assertThat(logger1.getLevel(), is(Level.ERROR));
        assertThat(logger1.getFormattedMessage(),
                is("consumeremail.EmailServiceImpl:sendEmailForVerification(obj), erro ao enviar o e-mail para 'email.ok@email.com', MailException 'Erro retornado'"));
    }
}