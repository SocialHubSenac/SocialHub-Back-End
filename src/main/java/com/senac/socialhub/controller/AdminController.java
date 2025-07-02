package com.senac.socialhub.controller;

import com.senac.socialhub.controller.dto.UsuarioResponseDTO;
import com.senac.socialhub.entity.Usuario;
import com.senac.socialhub.enums.Role;
import com.senac.socialhub.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listar();
        List<UsuarioResponseDTO> dtos = usuarios.stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getRole().name()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> alterarRole(@PathVariable Long id, @RequestBody String novaRole) {
        usuarioService.alterarRole(id, Role.valueOf(novaRole));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/bloquear")
    public ResponseEntity<Void> bloquear(@PathVariable Long id) {
        usuarioService.bloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/desbloquear")
    public ResponseEntity<Void> desbloquear(@PathVariable Long id) {
        usuarioService.desbloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
