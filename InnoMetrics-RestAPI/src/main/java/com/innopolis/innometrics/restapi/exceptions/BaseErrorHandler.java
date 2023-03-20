package com.innopolis.innometrics.restapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseErrorHandler {

    /**
     * Source: <a href="https://dev.to/cuongld2/create-apis-with-jwt-authorization-using-spring-boot-24f9">...</a>
     */
    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMsg());
    }
}
