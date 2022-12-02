package br.com.ernanilima.producer.resource;

import br.com.ernanilima.producer.ProducerTestIT;
import br.com.ernanilima.shared.dto.EmailForVerificationDTO;
import br.com.ernanilima.shared.dto.EmailToSupportDTO;
import br.com.ernanilima.shared.test.builder.EmailForVerificationBuilder;
import br.com.ernanilima.shared.test.builder.EmailToSupportBuilder;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static br.com.ernanilima.producer.utils.I18n.*;
import static java.text.MessageFormat.format;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProducerResourceTestIT extends ProducerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    @DisplayName("Deve retornar sucesso para o endpoint /send/email-to-support")
    void sendEmailToSupport_Must_Return_Success() throws Exception {
        String dtoJson = gson.toJson(EmailToSupportBuilder.create());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-to-support")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))

                // deve retornar o Status 200
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar um erro por nao enviar body para o endpoint /send/email-to-support")
    void sendEmailToSupport_Must_Return_An_Error_For_Not_Sending_Body() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-to-support")
                        .contentType(MediaType.APPLICATION_JSON))

                // deve retornar o Status 400
                .andExpect(status().isBadRequest())
                // deve retornar uma excecao 'HttpMessageNotReadableException'
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
                // deve retornar o titulo de orientacao do erro
                .andExpect(jsonPath("$.error", is(getMessage(TTL_INVALID_DATA))))
                // deve retornar a mensagem de orientacao do erro
                .andExpect(jsonPath("$.message", is(getMessage(EXC_INVALID_DATA))));
    }

    @Test
    @DisplayName("Deve retornar um erro por enviar body sem valores para o endpoint /send/email-to-support")
    void sendEmailToSupport_Must_Return_An_Error_For_Sending_Body_Without_Values() throws Exception {
        String dtoJson = gson.toJson(EmailToSupportDTO.builder().build());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-to-support")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))

                // deve retornar o Status 422
                .andExpect(status().isUnprocessableEntity())
                // deve retornar uma excecao 'MethodArgumentNotValidException'
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                // deve retornar o titulo de orientacao do erro
                .andExpect(jsonPath("$.error", is(getMessage(TTL_VALIDATION))))
                // deve retornar a mensagem de orientacao do erro
                .andExpect(jsonPath("$.message", is(format(getMessage(EXC_QUANTITY_OF_ERRORS), 3))))
                // deve retornar a quantiadde de erro(s)
                .andExpect(jsonPath("$.errors.*", hasSize(3)))

                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("subject") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("subject") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("sender") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("sender") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("message") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("message") + "')].message",
                        contains(getMessage("empty.field"))));
    }

    @Test
    @DisplayName("Deve retornar um erro por enviar o email(sender) invalido para o endpoint /send/email-to-support")
    void sendEmailToSupport_Must_Return_An_Error_For_Sending_The_EmailSENDER_Invalid() throws Exception {
        String dtoJson = gson.toJson(EmailToSupportBuilder.createWithInvalidEmail());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-to-support")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))

                // deve retornar o Status 422
                .andExpect(status().isUnprocessableEntity())
                // deve retornar uma excecao 'MethodArgumentNotValidException'
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                // deve retornar o titulo de orientacao do erro
                .andExpect(jsonPath("$.error", is(getMessage(TTL_VALIDATION))))
                // deve retornar a mensagem de orientacao do erro
                .andExpect(jsonPath("$.message", is(format(getMessage(EXC_QUANTITY_OF_ERRORS), 1))))
                // deve retornar a quantiadde de erro(s)
                .andExpect(jsonPath("$.errors.*", hasSize(1)))

                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("sender") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("sender") + "')].message",
                        contains(getMessage("invalid.email"))));
    }

    @Test
    @DisplayName("Deve retornar sucesso para o endpoint /send/email-for-verification")
    void sendEmailForVerification_Must_Return_Success() throws Exception {
        String dtoJson = gson.toJson(EmailForVerificationBuilder.create());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-for-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))

                // deve retornar o Status 200
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar um erro por nao enviar body para o endpoint /send/email-for-verification")
    void sendEmailForVerification_Must_Return_An_Error_For_Not_Sending_Body() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-for-verification")
                        .contentType(MediaType.APPLICATION_JSON))

                // deve retornar o Status 400
                .andExpect(status().isBadRequest())
                // deve retornar uma excecao 'HttpMessageNotReadableException'
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
                // deve retornar o titulo de orientacao do erro
                .andExpect(jsonPath("$.error", is(getMessage(TTL_INVALID_DATA))))
                // deve retornar a mensagem de orientacao do erro
                .andExpect(jsonPath("$.message", is(getMessage(EXC_INVALID_DATA))));
    }

    @Test
    @DisplayName("Deve retornar um erro por enviar body sem valores para o endpoint /send/email-for-verification")
    void sendEmailForVerification_Must_Return_An_Error_For_Sending_Body_Without_Values() throws Exception {
        String dtoJson = gson.toJson(EmailForVerificationDTO.builder().build());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-for-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))

                // deve retornar o Status 422
                .andExpect(status().isUnprocessableEntity())
                // deve retornar uma excecao 'MethodArgumentNotValidException'
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                // deve retornar o titulo de orientacao do erro
                .andExpect(jsonPath("$.error", is(getMessage(TTL_VALIDATION))))
                // deve retornar a mensagem de orientacao do erro
                .andExpect(jsonPath("$.message", is(format(getMessage(EXC_QUANTITY_OF_ERRORS), 6))))
                // deve retornar a quantiadde de erro(s)
                .andExpect(jsonPath("$.errors.*", hasSize(6)))

                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("subject") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("subject") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("application") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("application") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("emailForVerification") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("emailForVerification") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("message") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("message") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("securityLink") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("securityLink") + "')].message",
                        contains(getMessage("empty.field"))))
                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("securityCode") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("securityCode") + "')].message",
                        contains(getMessage("empty.field"))));
    }

    @Test
    @DisplayName("Deve retornar um erro por enviar o email(emailForVerification) invalido para o endpoint /send/email-for-verification")
    void sendEmailForVerification_Must_Return_An_Error_For_Sending_The_EmailEMAILFORVERIFICATION_Invalid() throws Exception {
        String dtoJson = gson.toJson(EmailForVerificationBuilder.createWithInvalidEmail());
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/send/email-for-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoJson))

                // deve retornar o Status 422
                .andExpect(status().isUnprocessableEntity())
                // deve retornar uma excecao 'MethodArgumentNotValidException'
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                // deve retornar o titulo de orientacao do erro
                .andExpect(jsonPath("$.error", is(getMessage(TTL_VALIDATION))))
                // deve retornar a mensagem de orientacao do erro
                .andExpect(jsonPath("$.message", is(format(getMessage(EXC_QUANTITY_OF_ERRORS), 1))))
                // deve retornar a quantiadde de erro(s)
                .andExpect(jsonPath("$.errors.*", hasSize(1)))

                // deve retornar o nome o campo e a mensagem de erro
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("emailForVerification") + "')]").exists())
                .andExpect(jsonPath("$.errors[?(@.fieldName == '" + getMessage("emailForVerification") + "')].message",
                        contains(getMessage("invalid.email"))));
    }
}