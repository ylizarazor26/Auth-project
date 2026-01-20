package com.auth_service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * DTO de respuesta para operaciones de autenticación.
 *
 * <p>Se devuelve tras un registro o login exitoso,
 * incluyendo el token JWT y datos del usuario.</p>
 *
 * Campos:
 * <ul>
 *   <li>{@code token}: Token JWT generado.</li>
 *   <li>{@code username}: Nombre de usuario autenticado.</li>
 *   <li>{@code userId}: Identificador único del usuario.</li>
 *   <li>{@code message}: Mensaje descriptivo del resultado.</li>
 * </ul>
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Integer userId;
    private String message;
}