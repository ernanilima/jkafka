package br.com.ernanilima.shared.dto;

import br.com.ernanilima.shared.utils.Validation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailForVerificationDTO implements Serializable {
    @NotEmpty(message = "{empty.field}")
    private String subject;
    @NotEmpty(message = "{empty.field}")
    private String application;
    @NotEmpty(message = "{empty.field}")
    @Email(regexp = Validation.EMAIL_REGEX, message = "{invalid.email}")
    private String emailForVerification;
    @NotEmpty(message = "{empty.field}")
    private String message;
    @NotEmpty(message = "{empty.field}")
    private String securityLink;
    @NotEmpty(message = "{empty.field}")
    private String securityCode;

}
