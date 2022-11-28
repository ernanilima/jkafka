package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.consumeremail.service.EmailService;
import br.com.ernanilima.consumeremail.service.PrepareMailMessage;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailServiceImpl extends PrepareMailMessage implements EmailService {

    private final String CLASS_NAME = "consumeremail." + this.getClass().getSimpleName();

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmailToSupport(EmailToSupportDTO dto) {
        log.info("{}, iniciando envio do e-mail de '{}' com a mensagem '{}'",
                CLASS_NAME, dto.getSender(), dto.getMessage());

        try {
            emailSender.send(super.prepareSimpleMailMessage(dto));

            log.info("{}, e-mail de '{}' foi enviado",
                    CLASS_NAME, dto.getSender());
        } catch (MailSendException e) {
            log.error("{}, erro ao enviar o e-mail de '{}', MailException '{}'",
                    CLASS_NAME, dto.getSender(), e.getMessage());
        }
    }
}
