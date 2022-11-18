package br.com.ernanilima.producer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Objects;
import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18n {

    // TITLE FOR ERROR
    public static final String TTL_VALIDATION_ERROR = "title.validation.error";
    public static final String TTL_INVALID_DATA = "title.invalid.data";

    // EXCEPTIONS
    public static final String EXC_QUANTITY_OF_ERRORS = "exc.quantity.of.errors";
    public static final String EXC_INVALID_DATA = "exc.invalid.data";

    public static String getFieldName(String s) {
        return getMessage(s);
    }

    public static String getMessage(String s) {
        Objects.requireNonNull(s);
        return ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale())
                .getString(s.toLowerCase());
    }
}
