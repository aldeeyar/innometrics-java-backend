package com.innopolis.innometrics.restapi.exceptions;

public class ValidationException extends RuntimeException {
    /**
     * Source: <a href="https://dev.to/cuongld2/create-apis-with-jwt-authorization-using-spring-boot-24f9">...</a>
     */
    private static final long serialVersionUID = 1L;
    private final String msg;

    public ValidationException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
