package br.com.ernanilima.producer.builder;

import br.com.ernanilima.producer.dto.EmailDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailDTOBuilder {

    public static EmailDto create() {
        return EmailDto.builder()
                .sender("email1@email.com.br")
                .message("Mensagem para enviar")
                .build();
    }
}
