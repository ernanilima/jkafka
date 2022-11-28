package br.com.ernanilima.shared.dto;

import br.com.ernanilima.shared.utils.Environment;

public interface EmailDTO {

    default String getTo() {
        return Environment.RECIPIENT;
    }

    String getFrom();

    String getSubject();

    String getMessage();

}

