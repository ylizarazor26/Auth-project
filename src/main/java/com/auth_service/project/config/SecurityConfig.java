package com.auth_service.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración principal de seguridad.
 *
 * <p>Importancia:</p>
 * <ul>
 *   <li>Establece un esquema sin estado ({@code stateless}) para las sesiones</li>
 *   <li>Permite acceso público a los endpoints de autenticación
 *       ({@code /api/auth/**}) y al endpoint de error.</li>
 *   <li>Integra el filtro {@link JwtFilter} antes del filtro estándar
 *       {@link UsernamePasswordAuthenticationFilter} para validar tokens.</li>
 *   <li>Configura el uso de {@link BCryptPasswordEncoder} para encriptar contraseñas,
 *       reforzando la seguridad de las credenciales almacenadas.</li>
 *
 *   <li>{@code securityFilterChain}: Configura las reglas de seguridad HTTP.</li>
 *   <li>{@code passwordEncoder}: Proporciona un mecanismo seguro de encriptación
 *       de contraseñas.</li>
 * </ul>
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/auth/users")
                        .hasAuthority("READ")

                        .requestMatchers(HttpMethod.POST, "/api/auth/register")
                        .hasAuthority("CREATE")

                        .requestMatchers(HttpMethod.DELETE, "/api/auth/users/**")
                        .hasAuthority("DELETE")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
