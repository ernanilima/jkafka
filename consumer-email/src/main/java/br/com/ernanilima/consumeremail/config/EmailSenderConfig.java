package br.com.ernanilima.consumeremail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailSenderConfig {

    @Value("${default.sender.smtp.noreply}")
    private String senderSmtpNoreply;
    @Value("${default.password.smtp.noreply}")
    private String passwordSmtpNoreply;

    @Bean
    public JavaMailSender javaMailSender() {
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.starttls.required", "true");
        mailProperties.put("mail.smtp.socketFactory.port", "465");
        mailProperties.put("mail.smtp.debug", "false");
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.fallback", "false");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost("mail.ernanilima.com.br");
        mailSender.setPort(465);
        mailSender.setProtocol("smtp");
        mailSender.setUsername(senderSmtpNoreply);
        mailSender.setPassword(passwordSmtpNoreply);

        return mailSender;
    }
}
