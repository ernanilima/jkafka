package br.com.ernanilima.shared.dto;

import br.com.ernanilima.shared.utils.Validation;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailToSupportDTO implements Serializable, EmailDTO {

    @NotEmpty(message = "{empty.field}")
    private String subject;
    @NotEmpty(message = "{empty.field}")
    @Email(regexp = Validation.EMAIL_REGEX, message = "{invalid.email}")
    private String sender;
    @NotEmpty(message = "{empty.field}")
    private String message;

    @Override
    public String getFrom() {
        return this.sender;
    }
}
