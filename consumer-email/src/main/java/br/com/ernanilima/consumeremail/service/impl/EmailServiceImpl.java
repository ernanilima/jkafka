package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.consumeremail.service.EmailService;
//import br.com.ernanilima.producer.dto.EmailDTO;
import br.com.ernanilima.sharedlibrary.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    private final String CLASS_NAME = this.getClass().getPackageName() + "." + this.getClass().getSimpleName();

    @Override
    public void send(EmailDTO dto) {
        log.info("{}, iniciando envio do e-mail de '{}' com a mensagem '{}'",
                CLASS_NAME, dto.getSender(), dto.getMessage());
    }
}
