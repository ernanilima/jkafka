package br.com.ernanilima.producer.resource;

import br.com.ernanilima.producer.service.EmailService;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/send")
public class ProducerResource {

    private final EmailService emailService;

    @PostMapping(value = "/email-to-support")
    public ResponseEntity<Void> sendEmailToSupport(@Valid @RequestBody EmailToSupportDTO dto) {
        log.info("{} '/send/email-to-support', enviar e-mail de '{}'",
                this.getClass().getSimpleName(), dto.getSender());

        emailService.sendEmailToSupport(dto);

        return ResponseEntity.ok().build();
    }
}
