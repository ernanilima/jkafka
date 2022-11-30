package br.com.ernanilima.producer.service;

import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;

public interface EmailService {
    void sendEmailToSupport(EmailToSupportDTO dto);

    void sendEmailForVerification(EmailForVerificationDTO dto);
}