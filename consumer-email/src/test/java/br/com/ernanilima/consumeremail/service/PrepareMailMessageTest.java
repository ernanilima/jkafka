package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import br.com.ernanilima.shared.test.builder.EmailForVerificationBuilder;
import br.com.ernanilima.shared.test.builder.EmailToSupportBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class PrepareMailMessageTest {

    @Mock
    private PrepareMailMessage prepareMailMessageMock;

    @Mock
    private JavaMailSender mailSenderMock;
    @Mock
    private Configuration configMock;

    private final String SENDER_NO_REPLY = "noreply@email.com";

    @BeforeEach
    void setup() {
        prepareMailMessageMock = mock(PrepareMailMessage.class, Mockito.CALLS_REAL_METHODS);
        prepareMailMessageMock.setConfig(configMock);
        setField(prepareMailMessageMock, "senderNoreply", SENDER_NO_REPLY);
    }

    @Test
    @DisplayName("Deve retornar os dados do e-mail (prepareSimpleMailMessage)")
    void prepareSimpleMailMessage_Must_Return_Email_Data() {
        EmailToSupportDTO dtoMock = mock(EmailToSupportDTO.class, Mockito.RETURNS_DEEP_STUBS);
        dtoMock = EmailToSupportBuilder.updateMock(dtoMock);
        String recipient = "support@email.com";

        when(dtoMock.getTo()).thenReturn(recipient);

        SimpleMailMessage result = prepareMailMessageMock.prepareSimpleMailMessage(dtoMock);

        assertNotNull(result);

        assertThat(result.getSubject(), is(dtoMock.getSubject()));
        assertThat(result.getFrom(), is(dtoMock.getSender()));
        assertThat(Objects.requireNonNull(result.getTo())[0], is(recipient));
        assertThat(result.getText(), is(dtoMock.getMessage()));

        verify(dtoMock, times(1)).getTo();
    }

    @Test
    @DisplayName("Deve retornar os dados do e-mail (prepareMimeMessageForVerification)")
    void prepareMimeMessageForVerification_Must_Return_Email_Data() throws IOException, MessagingException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        when(mailSenderMock.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        when(configMock.getTemplate(anyString())).thenReturn(mock(Template.class));

        MimeMessage result = prepareMailMessageMock.prepareMimeMessageForVerification(mailSenderMock, dto);

        assertNotNull(result);

        assertThat(result.getSubject(), is(dto.getSubject()));
        assertThat(result.getFrom()[0].toString(), is(SENDER_NO_REPLY));
        assertThat(result.getAllRecipients()[0].toString(), is(dto.getEmailForVerification()));

        verify(mailSenderMock, times(1)).createMimeMessage();
        verifyNoMoreInteractions(mailSenderMock);
        verify(configMock, times(1)).getTemplate(anyString());
        verifyNoMoreInteractions(configMock);
    }

    @Test
    @DisplayName("Deve retornar um erro por nao obter MimeMessageHelper (prepareMimeMessageForVerification)")
    void prepareMimeMessageForVerification_Must__Return_An_Error_For_Not_Getting_MimeMessageHelper() throws MessagingException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        when(mailSenderMock.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        doThrow(new MessagingException("Error getting message")).when(prepareMailMessageMock).createMimeMessageHelper(any(MimeMessage.class));

        MessagingException exception = assertThrows(MessagingException.class, () -> prepareMailMessageMock.prepareMimeMessageForVerification(mailSenderMock, dto));
        assertThat(exception.getMessage(), is("Error getting message"));

        verify(mailSenderMock, times(1)).createMimeMessage();
        verifyNoMoreInteractions(mailSenderMock);
    }

    @Test
    @DisplayName("Deve retornar o template para o e-mail (prepareTemplateForVerification)")
    void prepareTemplateForVerification_Must_Return_The_Template_To_The_Email() throws IOException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        when(configMock.getTemplate(anyString())).thenReturn(mock(Template.class));

        String result = prepareMailMessageMock.prepareTemplateForVerification(dto);

        assertNotNull(result);

        verify(configMock, times(1)).getTemplate(anyString());
        verifyNoMoreInteractions(configMock);
    }

    @Test
    @DisplayName("Deve retornar um erro por nao obter o template do e-mail (prepareTemplateForVerification)")
    void prepareTemplateForVerification_Must_Return_An_Error_For_Not_Getting_The_Email_Template() throws TemplateException, IOException {
        EmailForVerificationDTO dto = EmailForVerificationBuilder.create();

        Template template = Mockito.mock(Template.class);

        when(configMock.getTemplate(anyString())).thenReturn(template);

        doThrow(new TemplateException("Error getting template", null)).when(template).process(any(), any(Writer.class));

        TemplateException exception = assertThrows(TemplateException.class, () -> prepareMailMessageMock.prepareTemplateForVerification(dto));
        assertThat(exception.getMessage(), is("Error getting template"));

        verify(configMock, times(1)).getTemplate(anyString());
        verifyNoMoreInteractions(configMock);
    }
}