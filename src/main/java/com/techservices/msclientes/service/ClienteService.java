package com.techservices.msclientes.service;

import com.techservices.msclientes.dto.ClienteRequest;
import com.techservices.msclientes.exception.ApiException;
import com.techservices.msclientes.model.Cliente;
import com.techservices.msclientes.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CAPA SERVICIO: logica de negocio del CRUD de clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /** READ - todos */
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    /** READ - uno */
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe un cliente con ID " + id));
    }

    /** CREATE */
    @Transactional
    public Cliente crear(ClienteRequest request) {
        if (clienteRepository.existsByDni(request.dni())) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un cliente con el DNI " + request.dni());
        }
        Cliente cliente = new Cliente();
        copiarDatos(request, cliente);
        return clienteRepository.save(cliente);
    }

    /** UPDATE */
    @Transactional
    public Cliente actualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarPorId(id);
        if (clienteRepository.existsByDniAndIdNot(request.dni(), id)) {
            throw new ApiException(HttpStatus.CONFLICT, "El DNI " + request.dni() + " ya pertenece a otro cliente");
        }
        copiarDatos(request, cliente);
        return clienteRepository.save(cliente);
    }

    /** DELETE */
    @Transactional
    public Cliente eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
        return cliente;
    }

    private void copiarDatos(ClienteRequest request, Cliente cliente) {
        cliente.setNombres(request.nombres().trim());
        cliente.setApellidos(request.apellidos().trim());
        cliente.setDni(request.dni());
        cliente.setCorreo(request.correo().trim().toLowerCase());
        cliente.setTelefono(request.telefono());
    }
}
