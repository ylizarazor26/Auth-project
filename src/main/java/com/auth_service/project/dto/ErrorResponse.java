package com.auth_service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO de respuesta para representar errores.
 *
 * <p>Se utiliza en los controladores para devolver mensajes
 * claros y consistentes cuando ocurre una excepción.</p>
 *
 * Campo:
 * <ul>
 *   <li>{@code error}: Mensaje descriptivo del error.</li>
 * </ul>
 */

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String error;
}