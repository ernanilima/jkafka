package br.com.ernanilima.producer.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.MissingResourceException;

import static br.com.ernanilima.producer.utils.I18n.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class I18nTest {

    @Test
    @DisplayName("Deve retornar um erro para chave invalida para I18n")
    void getFieldNameOrgetMessage_Must_Return_An_Error_For_Invalid_Key_In_I18n() {
        assertThrows(NullPointerException.class, () -> getFieldName(null));
        assertThrows(MissingResourceException.class, () -> getFieldName(""));
        assertThrows(MissingResourceException.class, () -> getFieldName("invalidName"));

        assertThrows(NullPointerException.class, () -> getMessage(null));
        assertThrows(MissingResourceException.class, () -> getMessage(""));
        assertThrows(MissingResourceException.class, () -> getMessage("invalidName"));
    }

    @Test
    @DisplayName("Deve retornar o nome do campo em pt_BR")
    void getFieldName_Must_Return_The_Field_Name_In_PTBR() {
        String subject = "subject";
        assertEquals("assunto", getFieldName(subject));
        String sender = "sender";
        assertEquals("remetente", getFieldName(sender));
        String message = "message";
        assertEquals("mensagem", getFieldName(message));
        String application = "application";
        assertEquals("aplicação", getFieldName(application));
        String emailForVerification = "emailForVerification";
        assertEquals("email para verificação", getFieldName(emailForVerification));
        String securityLink = "securityLink";
        assertEquals("link de segurança", getFieldName(securityLink));
        String securityCode = "securityCode";
        assertEquals("códigos de segurança", getFieldName(securityCode));
    }

    @Test
    @DisplayName("Deve retornar a mensagem em pt_BR para TTL_VALIDATION")
    void getMessage_Must_Return_The_Message_In_PTBR_To_TTL_VALIDATION() {
        assertEquals("Validação", getMessage(TTL_VALIDATION));
    }

    @Test
    @DisplayName("Deve retornar a mensagem em pt_BR para EXC_QUANTITY_OF_ERRORS")
    void getMessage_Must_Return_The_Message_In_PTBR_To_EXC_QUANTITY_OF_ERRORS() {
        assertEquals("Quantidade de erro(s): {0}.", getMessage(EXC_QUANTITY_OF_ERRORS));
    }

    @Test
    @DisplayName("Deve retornar a mensagem em pt_BR para TTL_INVALID_DATA")
    void getMessage_Must_Return_The_Message_In_PTBR_To_TTL_INVALID_DATA() {
        assertEquals("Valor inválido", getMessage(TTL_INVALID_DATA));
    }

    @Test
    @DisplayName("Deve retornar a mensagem em pt_BR para EXC_INVALID_DATA")
    void getMessage_Must_Return_The_Message_In_PTBR_To_EXC_INVALID_DATA() {
        assertEquals("Reveja o(s) valor(es) enviado(s).", getMessage(EXC_INVALID_DATA));
    }
}