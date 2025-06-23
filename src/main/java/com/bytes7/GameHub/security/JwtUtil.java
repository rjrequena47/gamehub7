package com.bytes7.GameHub.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "gamehub-secret"; // CAMBIAR POR UNA CONTRASEÑA SEGURA EN PRODUCCIÓN
    private static final long EXPIRATION_MS = 86400000; // 24h

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    public String getUsername(String token) {
        return validateToken(token).getSubject();
    }

    public String getRole(String token) {
        return validateToken(token).getClaim("role").asString();
    }
}
