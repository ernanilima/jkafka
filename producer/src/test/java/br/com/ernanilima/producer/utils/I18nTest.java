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
        String sender = "sender";
        assertEquals("remetente", getFieldName(sender));
        String message = "message";
        assertEquals("mensagem", getFieldName(message));
    }

    @Test
    @DisplayName("Deve retornar a mensagem em pt_BR para TTL_VALIDATION_ERROR")
    void getMessage_Must_Return_The_Message_In_PTBR_To_TTL_VALIDATION_ERROR() {
        assertEquals("Erro de validação", getMessage(TTL_VALIDATION_ERROR));
    }

    @Test
    @DisplayName("Deve retornar a mensagem em pt_BR para QUANTITY_OF_ERRORS")
    void getMessage_Must_Return_The_Message_In_PTBR_To_QUANTITY_OF_ERRORS() {
        assertEquals("Quantidade de erro(s): {0}.", getMessage(QUANTITY_OF_ERRORS));
    }
}