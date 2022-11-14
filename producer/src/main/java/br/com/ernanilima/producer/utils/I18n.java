package br.com.ernanilima.producer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18n {

    // TITLE FOR ERROR
    public static final String TTL_VALIDATION_ERROR = "title.validation.error";

    // EXCEPTIONS
    public static final String QUANTITY_OF_ERRORS = "exc.quantity.errors";

    public static String getFieldName(String s) {
        return getMessage(s);
    }

    public static String getMessage(String s) {
        return ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale())
                .getString(s.toLowerCase());
    }
}
