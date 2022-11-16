package br.com.ernanilima.producer.service;

import br.com.ernanilima.producer.dto.EmailDTO;

public interface EmailService {
    void send(EmailDTO dto);
}