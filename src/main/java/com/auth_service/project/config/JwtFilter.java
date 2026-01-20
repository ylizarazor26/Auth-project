package com.auth_service.project.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro de seguridad que intercepta cada petición HTTP para validar
 * el token JWT presente en el encabezado {@code Authorization}.
 *
 * <p>Extiende {@link OncePerRequestFilter}, lo que garantiza que se ejecute
 * una sola vez por cada solicitud.</p>
 *
 * <p>Importante</p>
 * <ul>
 *   <li>Verifica la existencia y validez de un token JWT en cada petición.</li>
 *   <li>Extrae el {@code username} del token mediante {@link JwtService}.</li>
 *   <li>Si el token es válido, registra la autenticación en el
 *       {@link SecurityContextHolder} para que el usuario sea reconocido
 *       como autenticado.</li>
 *   <li>Implementa un esquema de seguridad sin estado</li>
 * </ul>
 *
 * Flujo :
 * <ol>
 *   <li>Lee el encabezado {@code Authorization} de la petición.</li>
 *   <li>Verifica que comience con {@code Bearer }.</li>
 *   <li>Extrae el token y obtiene el {@code username}.</li>
 *   <li>Valida el token con {@link JwtService}.</li>
 *   <li>Si es correcto, crea un {@link UsernamePasswordAuthenticationToken}
 *       y lo registra en el contexto de seguridad.</li>
 *   <li>Si el token es inválido, se registra un mensaje en consola.</li>
 * </ol>
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            String username = jwtService.extraerUsername(token);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtService.validateToken(token, username)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.emptyList()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            System.out.println("JWT inválido: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
