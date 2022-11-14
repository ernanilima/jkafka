package br.com.ernanilima.producer.resource.exception;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        log.info("{}, erro de validacao em '{}' campos",
                this.getClass().getSimpleName(), e.getErrorCount());

        String errorTitle = getMessage(TTL_VALIDATION_ERROR);
        String message = format(getMessage(QUANTITY_OF_ERRORS), e.getErrorCount());
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
}