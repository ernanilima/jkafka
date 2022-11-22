package br.com.ernanilima.producer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class I18n {

    public static final Locale DEFAULT_LOCALE = new Locale("pt", "BR");

    // TITLE FOR ERROR
    public static final String TTL_VALIDATION = "title.validation";
    public static final String TTL_INVALID_DATA = "title.invalid.data";

    // EXCEPTIONS
    public static final String EXC_QUANTITY_OF_ERRORS = "exc.quantity.of.errors";
    public static final String EXC_INVALID_DATA = "exc.invalid.data";

    public static String getFieldName(String s) {
        return getMessage(s);
    }

    public static String getMessage(String s) {
        Objects.requireNonNull(s);
        return ResourceBundle.getBundle("messages", I18n.DEFAULT_LOCALE)
                .getString(s.toLowerCase());
    }
}
