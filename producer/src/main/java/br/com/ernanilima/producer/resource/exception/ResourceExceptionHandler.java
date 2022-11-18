package br.com.ernanilima.producer.resource.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static br.com.ernanilima.producer.utils.I18n.*;
import static java.text.MessageFormat.format;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
public class ResourceExceptionHandler {

    /**
     * Erros de validacao
     *
     * @param e MethodArgumentNotValidException
     * @param r HttpServletRequest
     * @return ResponseEntity<StandardError>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                         HttpServletRequest r) {
        log.warn("{}, alerta de validacao em '{}' campo(s)",
                this.getClass().getSimpleName(), e.getErrorCount());

        String errorTitle = getMessage(TTL_VALIDATION);
        String message = format(getMessage(EXC_QUANTITY_OF_ERRORS), e.getErrorCount());
        ErrorMultipleFields validarErro = ErrorMultipleFields.builder()
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT))
                .status(UNPROCESSABLE_ENTITY.value())
                .error(errorTitle)
                .message(message)
                .path(r.getRequestURI())
                .build();

        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                validarErro.addError(getFieldName(fieldError.getField()), fieldError.getDefaultMessage()));

        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(validarErro);
    }

    /**
     * Erros de valor invalido
     *
     * @param r HttpServletRequest
     * @return ResponseEntity<StandardError>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> httpMessageNotReadableException(HttpServletRequest r) {
        log.warn("{}, alerta de validacao no endpoint {}",
                this.getClass().getSimpleName(), r.getRequestURI());

        String errorTitle = getMessage(TTL_INVALID_DATA);
        StandardError standardError = StandardError.builder()
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT))
                .status(UNPROCESSABLE_ENTITY.value())
                .error(errorTitle)
                .message(getMessage(EXC_INVALID_DATA))
                .path(r.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }
}