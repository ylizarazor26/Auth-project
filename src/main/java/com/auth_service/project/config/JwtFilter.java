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
 * <p>Importante:</p>
 * <ul>
 *   <li>Garantiza que solo usuarios autenticados accedan a recursos protegidos.</li>
 *   <li>Extrae el {@code username} del token y lo coloca en el contexto de seguridad.</li>
 * </ul>
 *
 * Flujo:
 * <ol>
 *   <li>Verifica si existe un encabezado {@code Bearer}.</li>
 *   <li>Extrae y valida el token JWT.</li>
 *   <li>Si es válido, registra la autenticación en {@link SecurityContextHolder}.</li>
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

        String token = authHeader.substring(7);
        String username = jwtService.extraerUsername(token);

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null &&
                jwtService.validateToken(token, username) ) {

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

        filterChain.doFilter(request, response);
    }
}
