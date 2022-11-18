package br.com.ernanilima.producer.dto;

import br.com.ernanilima.producer.utils.Validation;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Builder
@Getter
public class EmailDTO implements Serializable {

    @NotEmpty(message = "{empty.field}")
    @Email(regexp = Validation.EMAIL_REGEX, message = "{invalid.email}")
    private String sender;

    @NotEmpty(message = "{empty.field}")
    private String message;

}
