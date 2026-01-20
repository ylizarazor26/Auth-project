package com.auth_service.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para mensajes genéricos de respuesta.
 */
@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private String message;
}