package com.techservices.msclientes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Respuesta JSON estandarizada para TODOS los endpoints del microservicio.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean exito,
        int status,
        String mensaje,
        T data,
        Integer tokensRestantes,
        Map<String, String> errores,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> ok(int status, String mensaje, T data, Integer tokensRestantes) {
        return new ApiResponse<>(true, status, mensaje, data, tokensRestantes, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status, String mensaje) {
        return new ApiResponse<>(false, status, mensaje, null, null, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> errorValidacion(Map<String, String> errores) {
        return new ApiResponse<>(false, 400, "Los datos enviados no son validos", null, null, errores, LocalDateTime.now());
    }
}
