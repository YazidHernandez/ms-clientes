package com.techservices.msclientes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Swagger / OpenAPI.
 * Agrega el boton "Authorize" para enviar x-api-key y el Bearer token desde la documentacion.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI msClientesOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-clientes - TechServices Peru")
                        .version("1.0.0")
                        .description("Microservicio independiente para la gestion de clientes (CRUD). "
                                + "Requiere los headers x-api-key y Authorization: Bearer <token>. "
                                + "Cada operacion exitosa descuenta 1 token del usuario.")
                        .contact(new Contact().name("TechServices Peru")))
                .components(new Components()
                        .addSecuritySchemes("apiKey", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("x-api-key"))
                        .addSecuritySchemes("bearerToken", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")));
    }
}
