package com.senac.socialhub.controller;

import com.senac.socialhub.controller.dto.CredenciaisLogin;
import com.senac.socialhub.controller.dto.UsuarioRequestDTO;
import com.senac.socialhub.entity.Usuario;
import com.senac.socialhub.security.TokenService;
import com.senac.socialhub.service.UsuarioService;
import com.senac.socialhub.exception.ValidacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          UsuarioService usuarioService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@Valid @RequestBody CredenciaisLogin dadosLogin,
                                        BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                error -> error.getField(),
                                error -> error.getDefaultMessage()
                        ));
                return ResponseEntity.badRequest().body(errors);
            }

            // Normalizar email para lowercase
            String emailNormalizado = dadosLogin.getEmail().toLowerCase().trim();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(emailNormalizado, dadosLogin.getSenha())
            );

            Usuario usuario = usuarioService.buscarPorEmail(emailNormalizado);

            String token = tokenService.gerarToken(usuario);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login realizado com sucesso");

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email ou senha incorretos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (DisabledException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Conta desativada");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro de autenticação");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioRequestDTO dadosUsuario,
                                       BindingResult result) {
        try {
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                error -> error.getField(),
                                error -> error.getDefaultMessage()
                        ));
                return ResponseEntity.badRequest().body(errors);
            }

            // Normalizar email antes de salvar
            if (dadosUsuario.getEmail() != null) {
                dadosUsuario.setEmail(dadosUsuario.getEmail().toLowerCase().trim());
            }

            Usuario novoUsuario = usuarioService.salvar(dadosUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuário cadastrado com sucesso");
            response.put("id", novoUsuario.getId());
            response.put("nome", novoUsuario.getNome());
            response.put("email", novoUsuario.getEmail());
            response.put("role", novoUsuario.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ValidacaoException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> obterPerfilUsuario(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Usuário não autenticado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String email = authentication.getName();
            Usuario usuario = usuarioService.buscarPorEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.getId());
            response.put("nome", usuario.getNome());
            response.put("email", usuario.getEmail());
            response.put("role", usuario.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // === ENDPOINT SIMPLIFICADO PARA RESET DE SENHA ===

    /**
     * Verifica se o email existe e permite redefinir a senha diretamente
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String novaSenha = request.get("novaSenha");

            // Validações
            if (email == null || email.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email é obrigatório");
                return ResponseEntity.badRequest().body(error);
            }

            if (novaSenha == null || novaSenha.length() < 6) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Senha deve ter pelo menos 6 caracteres");
                return ResponseEntity.badRequest().body(error);
            }

            // Normalizar email
            String emailNormalizado = email.toLowerCase().trim();

            // Verificar se o email existe
            if (!usuarioService.emailExiste(emailNormalizado)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Usuario usuario = usuarioService.buscarPorEmail(emailNormalizado);

            // Criptografar nova senha
            String senhaHasheada = passwordEncoder.encode(novaSenha);

            // Atualizar senha do usuário
            usuarioService.atualizarSenha(usuario.getId(), senhaHasheada);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Senha redefinida com sucesso");

            return ResponseEntity.ok(response);

        } catch (ValidacaoException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para verificar se um email existe no sistema
     */
    @GetMapping("/check-email")
    public ResponseEntity<?> verificarEmail(@RequestParam String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email é obrigatório");
                return ResponseEntity.badRequest().body(error);
            }

            // Normalizar email
            String emailNormalizado = email.toLowerCase().trim();

            boolean emailExiste = usuarioService.emailExiste(emailNormalizado);

            Map<String, Object> response = new HashMap<>();
            response.put("emailExiste", emailExiste);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}