package br.com.ernanilima.shared.test.builder;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailForVerificationBuilder {

    public static EmailForVerificationDTO create() {
        return EmailForVerificationDTO.builder()
                .subject("Assunto do e-mail")
                .application("JKafka")
                .emailForVerification("email.ok@email.com")
                .message("Mensagem OK")
                .securityLink("https://www.website.com.br")
                .securityCode("0918273645")
                .build();
    }
}
