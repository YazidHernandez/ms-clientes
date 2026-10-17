package com.techservices.msclientes.exception;

import com.techservices.msclientes.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Convierte cualquier error en una respuesta JSON estandarizada con su codigo HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Errores de negocio: 401, 403, 404, 409, 429 */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> manejarApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getStatus().value(), ex.getMessage()));
    }

    /** 400: fallo alguna validacion de ClienteRequest */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ApiResponse.errorValidacion(errores));
    }

    /** 400: el body no es un JSON valido */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "El cuerpo de la peticion no es un JSON valido"));
    }

    /** 400: el ID de la URL no es numerico */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        String mensaje = "El parametro '" + ex.getName() + "' tiene un valor invalido: " + ex.getValue();
        return ResponseEntity.badRequest().body(ApiResponse.error(400, mensaje));
    }

    /** 404: la ruta no existe */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> manejarRutaInexistente(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "La ruta /" + ex.getResourcePath() + " no existe"));
    }

    /** 405: metodo HTTP no permitido en esa ruta */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(405, "El metodo " + ex.getMethod() + " no esta permitido en esta ruta"));
    }

    /** 409: la BD rechazo el registro (por ejemplo, DNI duplicado) */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> manejarIntegridad(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, "El registro viola una restriccion de la base de datos (posible DNI duplicado)"));
    }

    /** 500: cualquier otro error */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error interno del servidor: " + ex.getMessage()));
    }
}
