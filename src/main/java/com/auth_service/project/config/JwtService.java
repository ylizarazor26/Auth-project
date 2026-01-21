package com.auth_service.project.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;



/**
 * Servicio encargado de la generación y validación de tokens JWT.
 *
 * <p>Importante:</p>
 * <ul>
 *   <li>Genera tokens firmados con clave secreta.</li>
 *   <li>Extrae información (claims) como {@code username} y {@code uid}.</li>
 *   <li>Valida expiración y consistencia del token.</li>
 * </ul>
 *
 * Configuración:
 * <ul>
 *   <li>{@code SECRET_KEY}: Clave secreta para firmar tokens.</li>
 *   <li>{@code EXPIRATION_TIME}: Tiempo de expiración (1 hora).</li>
 * </ul>
 *
 * Métodos:
 * <ul>
 *   <li>{@code generarToken}: Crea un JWT con username y userId.</li>
 *   <li>{@code extraerClaims}: Obtiene los claims del token.</li>
 *   <li>{@code extraerUsername}: Recupera el usuario del token.</li>
 *   <li>{@code validateToken}: Verifica validez y expiración.</li>
 * </ul>
 */
@Service
public class JwtService {

    private static final String SECRET_KEY =
            "CLAVE_SUPER_SECRETA_DE_32_CARACTERES_MINIMO";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generarToken(String username, Integer userId, List<String> roles, List<String> permissions) {
        return Jwts.builder()
                .setSubject(username)
                .claim("uid", userId)
                .claim("roles", roles)
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    // Validar si el token está expirado
    public boolean isTokenExpired(String token) {
        try {
            return extraerClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // Validar token completo
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extraerUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
}

