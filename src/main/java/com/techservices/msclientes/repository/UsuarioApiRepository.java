package com.techservices.msclientes.repository;

import com.techservices.msclientes.model.UsuarioApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CAPA REPOSITORIO: acceso a la tabla "usuarios_api".
 */
@Repository
public interface UsuarioApiRepository extends JpaRepository<UsuarioApi, Long> {

    Optional<UsuarioApi> findByApiKeyAndToken(String apiKey, String token);

    /** Descuenta 1 token de forma atomica, solo si el saldo es mayor a 0. */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UsuarioApi u SET u.tokensDisponibles = u.tokensDisponibles - 1 "
         + "WHERE u.id = :id AND u.tokensDisponibles > 0")
    int descontarToken(@Param("id") Long id);
}
