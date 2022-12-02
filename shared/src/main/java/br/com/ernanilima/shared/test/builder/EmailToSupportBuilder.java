package br.com.ernanilima.shared.test.builder;

import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailToSupportBuilder {

    public static EmailToSupportDTO create() {
        return EmailToSupportDTO.builder()
                .subject("Assunto do e-mail")
                .sender("email.ok@email.com")
                .message("Mensagem OK")
                .build();
    }
}
