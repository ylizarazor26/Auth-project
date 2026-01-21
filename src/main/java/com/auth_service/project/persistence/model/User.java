package com.auth_service.project.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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
 *   <li>{@code roles}: Conjunto de roles asociados al usuario.</li>
 * </ul>
 *
 *La relación es {@code @ManyToMany} con carga inmediata ({@code EAGER})
 *Se utiliza la tabla intermedia {@code user_roles} para vincular usuarios y roles.
 *Permite que un usuario tenga múltiples roles y que un rol sea compartido por varios usuarios.
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles = new HashSet<>();

}