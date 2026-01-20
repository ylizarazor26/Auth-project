package com.auth_service.project.persistence.repository;
import com.auth_service.project.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link User}.
 *
 * <p>Proporciona métodos de acceso a datos para la tabla {@code usuario}.</p>
 * Métodos:
 * <ul>
 *   <li>{@code findByUsername}: Busca un usuario por su nombre.</li>
 *   <li>{@code existsByUsername}: Verifica existencia de un usuario.</li>
 * </ul>
 */

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
