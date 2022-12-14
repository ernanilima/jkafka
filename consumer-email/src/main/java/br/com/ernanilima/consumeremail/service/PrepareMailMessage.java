package br.com.ernanilima.consumeremail.service;

import br.com.ernanilima.shared.dto.EmailDTO;
import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import freemarker.template.Configuration;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;
import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

public abstract class PrepareMailMessage {

    @Setter
    @Autowired
    private Configuration config;

    @Value("${default.sender.smtp.noreply}")
    private String senderNoreply;

    protected SimpleMailMessage prepareSimpleMailMessage(EmailDTO dto) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(dto.getSubject());
        message.setFrom(dto.getFrom()); // de
        message.setTo(dto.getTo()); // para
        message.setSentDate(new Date(System.currentTimeMillis()));
        message.setText(dto.getMessage());

        return message;
    }

    @SneakyThrows
    protected MimeMessage prepareMimeMessageForVerification(JavaMailSender javaMailSender, EmailForVerificationDTO dto) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = createMimeMessageHelper(message);

        helper.setSubject(dto.getSubject());
        helper.setFrom(senderNoreply); // de
        helper.setTo(dto.getEmailForVerification()); // para
        helper.setSentDate(new Date(System.currentTimeMillis()));
        helper.setText(prepareTemplateForVerification(dto), true);

        return helper.getMimeMessage();
    }

    protected MimeMessageHelper createMimeMessageHelper(MimeMessage message) throws MessagingException {
        return new MimeMessageHelper(message, MULTIPART_MODE_MIXED, "UTF-8");
    }

    @SneakyThrows
    protected String prepareTemplateForVerification(EmailForVerificationDTO dto) {
        return processTemplateIntoString(config.getTemplate("email-verification.ftl"), dto);
    }
}
