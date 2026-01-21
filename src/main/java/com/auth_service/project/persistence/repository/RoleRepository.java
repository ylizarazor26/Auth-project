package com.auth_service.project.persistence.repository;

import com.auth_service.project.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


/**
 * Repositorio JPA para la entidad {@link Role}.
 *
 * <p>Proporciona métodos de acceso a datos para la tabla {@code roles}.</p>
 * Métodos:
 * <ul>
 *   <li>{@code findByName}: Busca un rol por su nombre.</li>
 * </ul>
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
