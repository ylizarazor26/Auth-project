package com.auth_service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta que representa la información básica de un usuario.
 *
 * <p>Se utiliza principalmente en el endpoint {@code /api/auth/me}
 * para devolver los datos del usuario autenticado.</p>
 *
 * Campos:
 * <ul>
 *   <li>{@code id}: Identificador único del usuario.</li>
 *   <li>{@code username}: Nombre de usuario.</li>
 * </ul>
 */

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
}