package com.cards.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public Map<String, String> handleValidationException(
            WebExchangeBindException exception
    ) {
        return exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName(),
                        ObjectError::getDefaultMessage,
                        (existing, replacement) -> existing)
                );
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> serverErrorHandler(Exception exception) {
        ErrorHandler.LOGGER.error(exception.getMessage(), exception);
        return Map.of("error", exception.getMessage());
    }

}
