package br.com.ernanilima.producer.service;

import br.com.ernanilima.producer.dto.EmailDto;

public interface EmailService {
    void send(EmailDto emailDto);
}