package br.com.ernanilima.producer.dto;

import br.com.ernanilima.producer.utils.Validation;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class EmailDto implements Serializable {

    @NotEmpty(message = "{empty.field}")
    @Email(regexp = Validation.EMAIL_REGEX, message = "{invalid.email}")
    private String sender;

    @NotEmpty(message = "{empty.field}")
    private String message;

}
