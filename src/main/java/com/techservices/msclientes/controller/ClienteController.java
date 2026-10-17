package com.techservices.msclientes.controller;

import com.techservices.msclientes.dto.ApiResponse;
import com.techservices.msclientes.dto.ClienteRequest;
import com.techservices.msclientes.model.Cliente;
import com.techservices.msclientes.model.UsuarioApi;
import com.techservices.msclientes.service.AutenticacionService;
import com.techservices.msclientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CAPA CONTROLADOR: CRUD completo de clientes.
 * Todos los endpoints exigen x-api-key + Authorization: Bearer <token>
 * y descuentan 1 token solo cuando la operacion termina correctamente.
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "CRUD de clientes protegido con API Key y Token")
@SecurityRequirement(name = "apiKey")
@SecurityRequirement(name = "bearerToken")
public class ClienteController {

    private final ClienteService clienteService;
    private final AutenticacionService autenticacionService;

    public ClienteController(ClienteService clienteService, AutenticacionService autenticacionService) {
        this.clienteService = clienteService;
        this.autenticacionService = autenticacionService;
    }

    @Operation(summary = "READ - Listar todos los clientes")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Cliente>>> listar(
            @Parameter(hidden = true) @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization) {

        UsuarioApi usuario = autenticacionService.validarCredenciales(apiKey, authorization);
        List<Cliente> clientes = clienteService.listar();
        int tokensRestantes = autenticacionService.descontarToken(usuario);
        return ResponseEntity.ok(ApiResponse.ok(200, "Se encontraron " + clientes.size() + " clientes", clientes, tokensRestantes));
    }

    @Operation(summary = "READ - Obtener un cliente por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtener(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization) {

        UsuarioApi usuario = autenticacionService.validarCredenciales(apiKey, authorization);
        Cliente cliente = clienteService.buscarPorId(id);
        int tokensRestantes = autenticacionService.descontarToken(usuario);
        return ResponseEntity.ok(ApiResponse.ok(200, "Consulta realizada correctamente", cliente, tokensRestantes));
    }

    @Operation(summary = "CREATE - Registrar un nuevo cliente")
    @PostMapping
    public ResponseEntity<ApiResponse<Cliente>> crear(
            @Valid @RequestBody ClienteRequest request,
            @Parameter(hidden = true) @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization) {

        UsuarioApi usuario = autenticacionService.validarCredenciales(apiKey, authorization);
        Cliente cliente = clienteService.crear(request);
        int tokensRestantes = autenticacionService.descontarToken(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(201, "Cliente registrado correctamente", cliente, tokensRestantes));
    }

    @Operation(summary = "UPDATE - Actualizar un cliente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request,
            @Parameter(hidden = true) @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization) {

        UsuarioApi usuario = autenticacionService.validarCredenciales(apiKey, authorization);
        Cliente cliente = clienteService.actualizar(id, request);
        int tokensRestantes = autenticacionService.descontarToken(usuario);
        return ResponseEntity.ok(ApiResponse.ok(200, "Cliente actualizado correctamente", cliente, tokensRestantes));
    }

    @Operation(summary = "DELETE - Eliminar un cliente por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> eliminar(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorization) {

        UsuarioApi usuario = autenticacionService.validarCredenciales(apiKey, authorization);
        Cliente cliente = clienteService.eliminar(id);
        int tokensRestantes = autenticacionService.descontarToken(usuario);
        return ResponseEntity.ok(ApiResponse.ok(200, "Cliente eliminado correctamente", cliente, tokensRestantes));
    }
}
