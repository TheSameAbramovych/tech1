package com.tech1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    @Value("${jwt.secret:good}")
    private String jwtSecret;

    public String generateToken(String login) {
        Date exp = Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
        Date now = new Date();
        return Base64.getEncoder().encodeToString(Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(login)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact().getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String getLoginFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    private Claims parseToken(String token) {
        token = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

}
