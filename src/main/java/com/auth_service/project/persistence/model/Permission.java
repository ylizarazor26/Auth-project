package com.auth_service.project.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entidad JPA representa un rol.
 *
 * <p>Se mapea a la tabla {@code permissions} en la base de datos y define
 * los diferentes permisos que un usuario puede tener.</p>
 *
 * <p>Importante:</p>
 * <ul>
 *   <li>Control de acceso basado en permissions.</li>
 *   <li>Asignación de permisos específicos a cada usuario.</li>
 * </ul>
 *
 * Campos:
 * <ul>
 *   <li>{@code id}: Identificador único autogenerado del permiso.</li>
 *   <li>{@code name}: Nombre único del permiso.</li>
 * </ul>
 */

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;
}
