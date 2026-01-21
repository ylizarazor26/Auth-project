package com.auth_service.project.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA representa un rol.
 *
 * <p>Se mapea a la tabla {@code roles} en la base de datos y define
 * los diferentes perfiles o permisos que un usuario puede tener.</p>
 *
 * <p>Importante:</p>
 * <ul>
 *   <li>Control de acceso basado en roles (RBAC).</li>
 *   <li>Asignación de permisos específicos a cada usuario.</li>
 *   <li>Diferencia privilegios entre usuarios.</li>
 * </ul>
 *
 * Campos:
 * <ul>
 *   <li>{@code id}: Identificador único autogenerado del rol.</li>
 *   <li>{@code name}: Nombre único del rol.</li>
 * </ul>
 */

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

}
