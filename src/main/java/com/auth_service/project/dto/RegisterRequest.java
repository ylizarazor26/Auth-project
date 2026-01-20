package com.auth_service.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de solicitud para el registro de un nuevo usuario.
 *
 * <p>Contiene las credenciales necesarias para crear una cuenta
 * y aplica validaciones para garantizar la consistencia de los datos.</p>
 *
 * Validaciones:
 * <ul>
 *   <li>{@code username}: Obligatorio, entre 3 y 50 caracteres.</li>
 *   <li>{@code password}: Obligatoria, mínimo 6 caracteres.</li>
 * </ul>
 *
 * Importancia:
 * <ul>
 *   <li>Evita registros con datos incompletos o inválidos.</li>
 * </ul>
 */

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}