package br.com.ernanilima.consumeremail.service.impl;

import br.com.ernanilima.consumeremail.service.EmailService;
import br.com.ernanilima.shared.dto.EmailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class EmailServiceImpl implements EmailService {

    private final String CLASS_NAME = this.getClass().getPackageName() + "." + this.getClass().getSimpleName();

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Value("${default.sender.smtp.noreply}")
    private String senderSmtpNoReply;
    @Value("${default.recipient}")
    private String recipient;

    @Override
    public void send(EmailDTO dto) {
        log.info("{}, iniciando envio do e-mail de '{}' com a mensagem '{}'",
                CLASS_NAME, dto.getSender(), dto.getMessage());

        SimpleMailMessage mailMessage = prepareSimpleMailMessage(dto);
        emailSender.send(mailMessage);
    }

    protected SimpleMailMessage prepareSimpleMailMessage(EmailDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderSmtpNoReply); // de
        message.setTo(recipient); // para
        message.setSubject(String.format("Sugestão de %s", dto.getSender()));
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText(dto.getMessage());

        return message;
    }
}
