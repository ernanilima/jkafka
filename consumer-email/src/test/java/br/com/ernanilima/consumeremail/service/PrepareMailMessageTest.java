package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrepareMailMessageTest {

    @Mock
    private PrepareMailMessage prepareMailMessageMock;
    @Mock
    private EmailToSupportDTO dtoMock;

    @BeforeEach
    void setup() {
        prepareMailMessageMock = mock(PrepareMailMessage.class, Mockito.CALLS_REAL_METHODS);

        dtoMock = mock(EmailToSupportDTO.class, Mockito.RETURNS_DEEP_STUBS);
        dtoMock.toBuilder()
                .subject("Assunto do e-mail").sender("email.ok@email.com").message("Mensagem OK").build();
    }

    @Test
    @DisplayName("Deve retornar os dados do e-mail")
    void prepareSimpleMailMessage_Must_Return_Email_Data() {
        String recipient = "support@email.com";
        when(dtoMock.getTo()).thenReturn(recipient);

        SimpleMailMessage result = prepareMailMessageMock.prepareSimpleMailMessage(dtoMock);

        assertNotNull(result);

        assertThat(result.getFrom(), is(dtoMock.getSender()));
        assertThat(Objects.requireNonNull(result.getTo())[0], is(recipient));
        assertThat(result.getSubject(), is(dtoMock.getSubject()));
        assertThat(result.getText(), is(dtoMock.getMessage()));
    }
}