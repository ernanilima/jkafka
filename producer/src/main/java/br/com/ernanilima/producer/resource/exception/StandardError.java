package br.com.ernanilima.producer.resource.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Modelo padrao do erro para exibir
 */
@Getter
@SuperBuilder
public class StandardError implements Serializable {
    private String timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}