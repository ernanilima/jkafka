package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.consumeremail.service.EmailService;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    private final String CLASS_NAME = "consumeremail." + this.getClass().getSimpleName();

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Value("${default.recipient}")
    private String recipient;

    @Override
    public void sendEmailToSupport(EmailToSupportDTO dto) {
        log.info("{}, iniciando envio do e-mail de '{}' com a mensagem '{}'",
                CLASS_NAME, dto.getSender(), dto.getMessage());

        SimpleMailMessage mailMessage = prepareSimpleMailMessage(dto);

        try {
            emailSender.send(mailMessage);

            log.info("{}, e-mail de '{}' foi enviado",
                    CLASS_NAME, dto.getSender());
        } catch (MailSendException e) {
            log.error("{}, erro ao enviar o e-mail de '{}', MailException '{}'",
                    CLASS_NAME, dto.getSender(), e.getMessage());
        }
    }

    protected SimpleMailMessage prepareSimpleMailMessage(EmailToSupportDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(dto.getSender()); // de
        message.setTo(recipient); // para
        message.setSubject(dto.getSubject());
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText(dto.getMessage());

        return message;
    }
}
