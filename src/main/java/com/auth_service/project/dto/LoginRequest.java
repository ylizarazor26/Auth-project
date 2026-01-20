package com.auth_service.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de solicitud para el inicio de sesión.
 *
 * <p>Transporta las credenciales enviadas por el cliente
 * al microservicio de autenticación.</p>
 *
 * Validaciones:
 * <ul>
 *   <li>{@code username}: Obligatorio, no puede estar vacío.</li>
 *   <li>{@code password}: Obligatoria, no puede estar vacía.</li>
 * </ul>
 *
 * Importante:
 * <ul>
 *   <li>Garantiza que las credenciales mínimas estén presentes.</li>
 *   <li>Permite validar al usuario y generar un token JWT.</li>
 * </ul>
 */
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}