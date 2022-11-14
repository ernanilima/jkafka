package br.com.ernanilima.producer.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailDto implements Serializable {

    private String emailFrom;

    private String message;

}
