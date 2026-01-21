package com.auth_service.project.service;

import com.auth_service.project.config.JwtService;
import com.auth_service.project.dto.AuthResponse;
import com.auth_service.project.dto.LoginRequest;
import com.auth_service.project.dto.RegisterRequest;
import com.auth_service.project.persistence.model.Permission;
import com.auth_service.project.persistence.model.Role;
import com.auth_service.project.persistence.model.User;
import com.auth_service.project.persistence.repository.RoleRepository;
import com.auth_service.project.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Servicio principal de autenticación
 *
 * <p>Gestiona el registro de nuevos usuarios, el inicio de sesión
 * y la recuperación de usuarios por nombre.</p>
 *
 * <p>Importancia:</p>
 * <ul>
 *   <li>Centraliza la lógica de autenticación y registro.</li>
 *   <li>Valida credenciales y evita duplicidad de usuarios.</li>
 *   <li>Encripta contraseñas con {@link PasswordEncoder} para mayor seguridad.</li>
 *   <li>Genera tokens JWT mediante {@link JwtService} para sesiones sin estado.</li>
 *   <li>Proporciona respuestas estandarizadas con {@link AuthResponse}.</li>
 * </ul>
 *
 * Flujo:
 * <ol>
 *   <li>El cliente envía credenciales (username, password).</li>
 *   <li>El servicio valida existencia y autenticidad.</li>
 *   <li>Si es correcto, se genera un JWT y se devuelve al cliente.</li>
 * </ol>
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>Pasos:</p>
     * <ul>
     *   <li>Verifica que el username no exista.</li>
     *   <li>Encripta la contraseña con {@link PasswordEncoder}.</li>
     *   <li>Guarda el usuario en la base de datos.</li>
     *   <li>Genera un token JWT asociado al nuevo usuario.</li>
     * </ul>
     *
     * @param request datos de registro (username, password).
     * @return {@link AuthResponse} con token, username, id y mensaje de éxito.
     * @throws RuntimeException si el username ya está en uso.
     */

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER no existe"));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.getRoles().add(userRole);

        User savedUser = userRepository.save(newUser);

        List<String> roles = savedUser.getRoles().stream()
                .map(Role::getName)
                .toList();

        List<String> permissions = savedUser.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();

        String token = jwtService.generarToken(
                savedUser.getUsername(),
                savedUser.getSerial(),
                roles,
                permissions
        );

        return new AuthResponse(
                token,
                savedUser.getUsername(),
                savedUser.getSerial(),
                "Usuario registrado exitosamente"
        );
    }

    /**
     * Autentica a un usuario existente en el sistema.
     *
     * <p>Pasos:</p>
     * <ul>
     *   <li>Busca el usuario por username.</li>
     *   <li>Verifica que la contraseña coincida con la almacenada.</li>
     *   <li>Genera un token JWT válido para el usuario.</li>
     * </ul>
     *
     * @param request credenciales de login (username, password).
     * @return {@link AuthResponse} con token, username, id y mensaje de éxito.
     * @throws RuntimeException si las credenciales son inválidas.
     */

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // ✅ ROLES
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        // ✅ PERMISOS
        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();

        String token = jwtService.generarToken(
                user.getUsername(),
                user.getSerial(),
                roles,
                permissions
        );

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getSerial(),
                "Login exitoso"
        );
    }


    /**
     * Recupera un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar.
     * @return {@link User} encontrado en la base de datos.
     * @throws RuntimeException si el usuario no existe.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}