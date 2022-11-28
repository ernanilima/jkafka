package resources;

import br.com.ernanilima.consumeremail.ConsumerEmailTestIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ApplicationPropertiesTestIT extends ConsumerEmailTestIT {

    @Autowired
    private Environment environment;

    @Test
    @DisplayName("Deve retornar a variavel de ambiente SMTP_NOREPLY_EMAIL_USER")
    public void environment_Default_Sender_Smtp_Noreply() {
        String senderNoreply = environment.getProperty("default.sender.smtp.noreply");
        assertThat(senderNoreply, is("noreply@email.com"));
    }

    @Test
    @DisplayName("Deve retornar a variavel de ambiente SMTP_NOREPLY_EMAIL_PASSWORD")
    public void environment_Default_Password_Smtp_Noreply() {
        String passwordNoreply = environment.getProperty("default.password.smtp.noreply");
        assertThat(passwordNoreply, is("0918273645"));
    }

    @Test
    @DisplayName("Deve retornar a variavel de ambiente SUPPORT_EMAIL_USER")
    public void environment_Default_Recipient() {
        String recipient = environment.getProperty("default.recipient");
        assertThat(recipient, is("support@email.com"));
    }
}
