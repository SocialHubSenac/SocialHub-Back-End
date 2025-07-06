package com.senac.socialhub.controller;

import com.senac.socialhub.controller.dto.CredenciaisLogin;
import com.senac.socialhub.security.TokenService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public String autenticar(@RequestBody CredenciaisLogin dadosLogin) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dadosLogin.getEmail(), dadosLogin.getSenha())
        );

        return tokenService.gerarToken(authentication.getName());
    }
}
