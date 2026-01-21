package com.auth_service.project.controller;

import com.auth_service.project.dto.*;
import com.auth_service.project.persistence.model.User;
import com.auth_service.project.service.AuthService;
import com.auth_service.project.service.UsuarioServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de autenticación de usuarios.
 * <p>Importante:</p>
 * <ul>
 *   <li>Centraliza las rutas de autenticación bajo {@code /api/auth}.</li>
 *   <li>Permite registrar nuevos usuarios y autenticar existentes.</li>
 *   <li>Devuelve tokens JWT y datos del usuario autenticado.</li>
 *   <li>Maneja respuestas de error estandarizadas mediante {@link ErrorResponse}.</li>
 * </ul>
 *
 * Endpoints:
 * <ul>
 *   <li>{@code GET /api/auth/test}: Endpoint de prueba para verificar disponibilidad.</li>
 *   <li>{@code GET /api/auth/me}: Devuelve información del usuario autenticado.</li>
 *   <li>{@code POST /api/auth/register}: Registra un nuevo usuario.</li>
 *   <li>{@code POST /api/auth/login}: Autentica un usuario existente.</li>
 *   <li>{@code DELETE /api/auth/me}: Elimina la información de un usuario.</li>
 * </ul>
 *
 * Flujo típico:
 * <ol>
 *   <li>El cliente envía credenciales o datos de registro.</li>
 *   <li>El controlador delega la lógica en {@link AuthService}.</li>
 *   <li>Se devuelve una respuesta con token JWT o mensaje de error.</li>
 * </ol>
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioServicio usuarioServicio;

    public AuthController(AuthService authService, UsuarioServicio usuarioServicio) {
        this.authService = authService;
        this.usuarioServicio = usuarioServicio;
    }

    /**
     * Endpoint de prueba para verificar que el servicio está activo.
     *
     * @return {@link ResponseEntity} con mensaje "Hello World".
     */
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello World");
    }

    /**
     * Obtiene el usuario actualmente autenticado a partir del contexto de seguridad.
     *
     * @param authentication objeto proporcionado por Spring Security con datos del usuario.
     * @return {@link UserResponse} con id y username, o {@link ErrorResponse} si no está autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = authService.getUserByUsername(username);

            UserResponse response = new UserResponse(
                    user.getSerial(),
                    user.getUsername()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse("No autenticado")
            );
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request datos de registro validados (username, password).
     * @return {@link AuthResponse} con token y datos del usuario, o {@link ErrorResponse} si falla.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(e.getMessage())
            );
        }
    }
    /**
     * Autentica un usuario existente en el sistema.
     *
     * @param request credenciales de login validadas (username, password).
     * @return {@link AuthResponse} con token y datos del usuario, o {@link ErrorResponse} si falla.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(e.getMessage())
            );
        }
    }

    /**
     * Elimina la cuenta del usuario autenticado.
     *
     * @param authentication datos del usuario autenticado
     * @return mensaje de confirmación
     */
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMyAccount(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("No autenticado"));
            }

            String username = authentication.getName();
            User user = authService.getUserByUsername(username);

            usuarioServicio.eliminar(
                    user.getSerial(),
                    "Eliminación voluntaria del usuario"
            );

            return ResponseEntity.ok(
                    new MessageResponse("Cuenta eliminada exitosamente")
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Error al eliminar cuenta: " + e.getMessage())
            );
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(usuarioServicio.obtenerTodos());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        usuarioServicio.eliminar(id, "Eliminado por administrador");
        return ResponseEntity.ok(new MessageResponse("Usuario eliminado"));
    }

}