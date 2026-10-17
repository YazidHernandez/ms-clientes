package com.techservices.msclientes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * CAPA MODELO: tabla "usuarios_api" (reutilizada del Ejercicio 1).
 */
@Entity
@Table(name = "usuarios_api")
public class UsuarioApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    @JsonIgnore
    @Column(name = "api_key", nullable = false, unique = true, length = 100)
    private String apiKey;

    @JsonIgnore
    @Column(nullable = false)
    private String token;

    @Column(name = "tokens_disponibles", nullable = false)
    private Integer tokensDisponibles;

    @Column(nullable = false, length = 20)
    private String estado;

    public UsuarioApi() {
    }

    public boolean estaActivo() {
        return "ACTIVO".equalsIgnoreCase(estado);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getTokensDisponibles() { return tokensDisponibles; }
    public void setTokensDisponibles(Integer tokensDisponibles) { this.tokensDisponibles = tokensDisponibles; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
