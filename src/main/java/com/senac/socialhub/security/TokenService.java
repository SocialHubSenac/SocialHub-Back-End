package com.senac.socialhub.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.expiration}")
    private String expiracao;

    public String gerarToken(String emailUsuario) {
        return Jwts.builder()
                .setSubject(emailUsuario)
                .setExpiration(new Date(System.currentTimeMillis() + expiracao))
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

}
