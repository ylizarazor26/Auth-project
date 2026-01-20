package com.auth_service.project.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA representa a un usuario dentro del sistema de autenticación.
 *
 * <p>Se mapea a la tabla {@code usuario} en la base de datos
 * contiene las credenciales básicas necesarias para el login.</p>
 *
 * Campos:
 * <ul>
 *   <li>{@code serial}: Identificador único autogenerado.</li>
 *   <li>{@code username}: Nombre de usuario.</li>
 *   <li>{@code password}: Contraseña.</li>
 * </ul>
 */

@NoArgsConstructor
@Setter
@Getter
@Table(name = "usuario")
@Entity

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serial;

    @Column(name = "username")
    public String username;
    @Column(name = "password")
    public String password;

}