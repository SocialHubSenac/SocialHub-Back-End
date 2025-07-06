package com.senac.socialhub.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public TokenFilter(TokenService tokenService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.tokenService = tokenService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (tokenService.tokenValido(token)) {
                String email = tokenService.pegarEmailUsuario(token);
                UserDetails usuario = usuarioAutenticadoService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken autenticacao = new UsernamePasswordAuthenticationToken(
                        usuario, null, usuario.getAuthorities());

                autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }
        }

        filterChain.doFilter(request, response);
    }
}
