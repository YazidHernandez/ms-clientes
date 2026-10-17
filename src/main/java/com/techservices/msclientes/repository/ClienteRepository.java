package com.techservices.msclientes.repository;

import com.techservices.msclientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA REPOSITORIO: acceso a la tabla "clientes".
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /** POST: evita registrar dos clientes con el mismo DNI. */
    boolean existsByDni(String dni);

    /** PUT: ¿otro cliente distinto a este ya usa ese DNI? */
    boolean existsByDniAndIdNot(String dni, Long id);
}
