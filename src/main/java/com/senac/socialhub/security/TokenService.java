package com.senac.socialhub.security;

import com.senac.socialhub.entity.Usuario;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.expiration}")
    private Long expiracao; // em minutos

    public String gerarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("nome", usuario.getNome());
        claims.put("instituicaoId", null); // Ajuste conforme sua entidade
        claims.put("role", usuario.getRole().name());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS512, segredo)
                .compact();
    }

    public boolean tokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(segredo).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String pegarEmailUsuario(String token) {
        return Jwts.parser()
                .setSigningKey(segredo)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(segredo)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpirationDate() {
        Instant instant = LocalDateTime.now()
                .plusMinutes(expiracao)
                .toInstant(ZoneOffset.of("-03:00"));
        return Date.from(instant);
    }
}
