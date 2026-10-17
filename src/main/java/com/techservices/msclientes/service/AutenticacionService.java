package com.techservices.msclientes.service;

import com.techservices.msclientes.exception.ApiException;
import com.techservices.msclientes.model.UsuarioApi;
import com.techservices.msclientes.repository.UsuarioApiRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CAPA SERVICIO: seguridad (API Key + Token) y control de consumo de tokens.
 * Misma regla del Ejercicio 1, ahora dentro del microservicio.
 */
@Service
public class AutenticacionService {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final UsuarioApiRepository usuarioApiRepository;

    public AutenticacionService(UsuarioApiRepository usuarioApiRepository) {
        this.usuarioApiRepository = usuarioApiRepository;
    }

    public UsuarioApi validarCredenciales(String apiKey, String authorization) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Falta el header obligatorio x-api-key");
        }
        if (authorization == null || !authorization.startsWith(PREFIJO_BEARER)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "El header Authorization es obligatorio y debe tener el formato: Bearer <token>");
        }
        String token = authorization.substring(PREFIJO_BEARER.length()).trim();

        UsuarioApi usuario = usuarioApiRepository.findByApiKeyAndToken(apiKey.trim(), token)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "API Key o Token invalidos"));

        if (!usuario.estaActivo()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "El usuario '" + usuario.getNombreUsuario() + "' esta inactivo");
        }
        if (usuario.getTokensDisponibles() <= 0) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,
                    "Has agotado tus tokens. Contacta al administrador para recargar tu saldo");
        }
        return usuario;
    }

    /** Descuenta 1 token. Se llama SOLO cuando la operacion termino bien. */
    @Transactional
    public int descontarToken(UsuarioApi usuario) {
        int filasActualizadas = usuarioApiRepository.descontarToken(usuario.getId());
        if (filasActualizadas == 0) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,
                    "Has agotado tus tokens. Contacta al administrador para recargar tu saldo");
        }
        return usuario.getTokensDisponibles() - 1;
    }
}
