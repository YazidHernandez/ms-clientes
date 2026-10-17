package com.techservices.msclientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos que llegan en el body de POST y PUT.
 * Se validan ANTES de guardar en MySQL (por el @Valid del controlador).
 */
public record ClienteRequest(

        @Schema(example = "Luis Alberto")
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no deben superar 100 caracteres")
        String nombres,

        @Schema(example = "Castro Paredes")
        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 100, message = "Los apellidos no deben superar 100 caracteres")
        String apellidos,

        @Schema(example = "48759612")
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 digitos")
        String dni,

        @Schema(example = "luis.castro@gmail.com")
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 150, message = "El correo no debe superar 150 caracteres")
        String correo,

        @Schema(example = "987654321")
        @NotBlank(message = "El telefono es obligatorio")
        @Pattern(regexp = "9\\d{8}", message = "El telefono debe tener 9 digitos y empezar con 9")
        String telefono
) {
}
