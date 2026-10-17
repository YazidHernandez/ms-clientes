package com.techservices.msclientes.exception;

import org.springframework.http.HttpStatus;

/**
 * Error de negocio con su codigo HTTP (401, 403, 404, 409, 429...).
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String mensaje) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
