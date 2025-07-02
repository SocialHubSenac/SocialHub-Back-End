package com.senac.socialhub.service;

import com.senac.socialhub.controller.dto.UsuarioRequestDTO;
import com.senac.socialhub.entity.Usuario;
import com.senac.socialhub.enums.Role;
import com.senac.socialhub.exception.ValidacaoException;
import com.senac.socialhub.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvar(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidacaoException("Já existe um usuário com este email.");
        }

        Usuario novo = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(Role.valueOf(dto.getTipo()))
                .build();

        return usuarioRepository.save(novo);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
    }

    public Usuario atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario existente = buscarPorId(id);
        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setSenha(passwordEncoder.encode(dto.getSenha()));
        existente.setRole(Role.valueOf(dto.getTipo()));
        return usuarioRepository.save(existente);
    }

    public void excluir(Long id) {
        Usuario u = buscarPorId(id);
        usuarioRepository.delete(u);
    }

    //PARTE DO PAINEL ADMIN

    public void alterarRole(Long id, Role novaRole) {
        Usuario usuario = buscarPorId(id);
        usuario.setRole(novaRole);
        usuarioRepository.save(usuario);
    }

    public void bloquearUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public void desbloquearUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        usuarioRepository.save(usuario);
    }
}
