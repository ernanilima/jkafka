package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.shared.dto.EmailDTO;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class PrepareMailMessage {

    protected SimpleMailMessage prepareSimpleMailMessage(EmailDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(dto.getFrom()); // de
        message.setTo(dto.getTo()); // para
        message.setSubject(dto.getSubject());
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText(dto.getMessage());

        return message;
    }
}
