package com.auth_service.project.service;

import com.auth_service.project.persistence.model.User;
import com.auth_service.project.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Servicio de gestión de usuarios.
 *
 * <p>Operaciones CRUD básicas de la entidad {@link User}</p>
 *
 * <p>Importante:</p>
 * <ul>
 *   <li>Centraliza la administración de usuarios.</li>
 *   <li>Encripta contraseñas con {@link PasswordEncoder} antes de persistirlas.</li>
 *   <li>Incluye trazas de auditoría simples mediante mensajes en consola.</li>
 *   <li>Garantiza consistencia y seguridad en las operaciones de creación y eliminación.</li>
 * </ul>
 *
 * Flujo:
 * <ol>
 *   <li>El servicio recibe una petición de gestión de usuarios.</li>
 *   <li>Valida existencia y aplica reglas de negocio (ej. encriptación de contraseñas).</li>
 *   <li>Ejecuta la operación en la base de datos a través del {@link UserRepository}.</li>
 * </ol>
 */

@Service
@Transactional
public class UsuarioServicio {

    private final UserRepository repositorio;
    private final PasswordEncoder passwordEncoder;


    public UsuarioServicio(UserRepository repositorio,
                           PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     *
     * @return lista de {@link User}.
     */
    public List<User> obtenerTodos() {
        return repositorio.findAll();
    }

    /**
     * Recupera un usuario por su identificador único.
     *
     * @param id identificador del usuario.
     * @return {@link User} correspondiente.
     * @throws RuntimeException si el usuario no existe.
     */
    public User obtenerPorId(Integer id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    /**
     * Guarda un usuario en la base de datos.
     *
     * <p>Si la contraseña no está encriptada (no comienza con {@code $2a$}),
     * se encripta automáticamente antes de persistir.</p>
     *
     * @param usuario entidad {@link User} a guardar.
     * @param motivo texto de auditoría para registrar la acción.
     * @return usuario persistido.
     */
    public User guardar(User usuario, String motivo) {

        if (!usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        System.out.println("Auditoría Usuario: " + motivo);
        return repositorio.save(usuario);
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param id identificador del usuario a eliminar.
     * @param motivo texto de auditoría para registrar la acción.
     * @throws RuntimeException si el usuario no existe.
     */
    public void eliminar(Integer id, String motivo) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("Usuario no existe");
        }
        System.out.println("Auditoría Usuario: eliminación por " + motivo);
        repositorio.deleteById(id);
    }
}
