package br.com.ernanilima.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Environment {
    /**
     * consumer-email/src/main/resources/application.properties:default.recipient
     */
    public static final String RECIPIENT = System.getenv("SUPPORT_EMAIL_USER");
}
