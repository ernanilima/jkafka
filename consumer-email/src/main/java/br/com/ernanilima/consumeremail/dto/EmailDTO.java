package br.com.ernanilima.consumeremail.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class EmailDTO implements Serializable {

    private String sender;

    private String message;

}
