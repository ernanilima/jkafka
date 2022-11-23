package br.com.ernanilima.producer.lombok;

import br.com.ernanilima.producer.resource.exception.ErrorMultipleFields;
import br.com.ernanilima.producer.resource.exception.StandardError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToStringTest {

    private String randomString;

    @BeforeEach
    void setup() {
        randomString = RandomStringUtils.randomAlphabetic(10);
    }

    @Test
    void standardError_Builder_toString() {
        String standardError = StandardError.builder().message(this.randomString).toString();
        assertTrue(standardError.contains(this.randomString));
    }

    @Test
    void errorMultipleFields_Builder_toString() {
        String errorMultipleFields = ErrorMultipleFields.builder().message(this.randomString).toString();
        assertTrue(errorMultipleFields.contains(this.randomString));
    }
}
