package com.auth_service.project.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT que se ejecuta una vez por cada petición HTTP.
 * Flujo:
 * <ol>
 *   <li>Se verifica si el encabezado {@code Authorization} contiene un token válido.</li>
 *   <li>Se extraen {@link Claims} y permisos asociados.</li>
 *   <li>Se crea un {@link UsernamePasswordAuthenticationToken} con las autoridades.</li>
 *   <li>Se establece el contexto de seguridad para el usuario autenticado.</li>
 *   <li>Si el token es inválido o no existe, se continúa la cadena de filtros sin autenticar.</li>
 *   <li>Los errores se registran en consola con un mensaje descriptivo.</li>
 * </ol>
 *
 * Dependencias:
 * <ul>
 *   <li>{@link JwtService}: Servicio encargado de extraer y validar información del token.</li>
 * </ul>
 */

@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Constructor que inyecta el servicio JWT.
     *
     * @param jwtService servicio para validación y extracción de datos del token.
     */

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Procesa cada petición HTTP verificando la validez del token JWT.
     *
     * @param request objeto {@link HttpServletRequest} con los datos de la petición.
     * @param response objeto {@link HttpServletResponse} para enviar la respuesta.
     * @param filterChain cadena de filtros de Spring Security.
     * @throws ServletException si ocurre un error en el procesamiento del filtro.
     * @throws IOException si ocurre un error de entrada/salida.
     */

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

                Claims claims = jwtService.extraerClaims(token);

                Object permsObj = claims.get("permissions");

                if (permsObj != null) {

                    List<SimpleGrantedAuthority> authorities =
                            ((List<?>) permsObj).stream()
                                    .map(Object::toString)
                                    .map(SimpleGrantedAuthority::new)
                                    .toList();


                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    authorities
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            System.out.println("JWT inválido: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

