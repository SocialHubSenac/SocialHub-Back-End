package com.senac.socialhub.service;

import com.senac.socialhub.controller.dto.UsuarioRequestDTO;
import com.senac.socialhub.entity.Ong;
import com.senac.socialhub.entity.Usuario;
import com.senac.socialhub.enums.Role;
import com.senac.socialhub.exception.ValidacaoException;
import com.senac.socialhub.repository.OngRepository;
import com.senac.socialhub.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final OngRepository ongRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          OngRepository ongRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.ongRepository = ongRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario salvar(UsuarioRequestDTO dto) {
        // Normalizar email antes de validar
        String emailNormalizado = dto.getEmail().toLowerCase().trim();

        // Validação de email duplicado
        if (usuarioRepository.findByEmail(emailNormalizado).isPresent()) {
            throw new ValidacaoException("Já existe um usuário com este email.");
        }

        boolean isOng = "ONG".equalsIgnoreCase(dto.getTipo());

        // Validações específicas para ONG
        if (isOng) {
            if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
                throw new ValidacaoException("CNPJ é obrigatório para ONG.");
            }
            if (ongRepository.existsByCnpj(dto.getCnpj().trim())) {
                throw new ValidacaoException("Já existe uma ONG cadastrada com este CNPJ.");
            }
            if (dto.getDescricao() == null || dto.getDescricao().trim().isEmpty()) {
                throw new ValidacaoException("Descrição é obrigatória para ONG.");
            }
        }

        // Criação do usuário
        Usuario novoUsuario = Usuario.builder()
                .nome(dto.getNome())
                .email(emailNormalizado)
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(isOng ? Role.ADMIN : Role.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Criação da ONG se necessário
        if (isOng) {
            Ong ong = Ong.builder()
                    .cnpj(dto.getCnpj().trim())
                    .descricao(dto.getDescricao().trim())
                    .usuario(usuarioSalvo)
                    .build();
            ongRepository.save(ong);
        }

        return usuarioSalvo;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
    }

    public Usuario buscarPorEmail(String email) {
        // Normalizar email antes de buscar
        String emailNormalizado = email.toLowerCase().trim();
        return usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario existente = buscarPorId(id);

        // Mapeamento correto do tipo para Role
        Role novaRole;
        if ("ONG".equalsIgnoreCase(dto.getTipo())) {
            novaRole = Role.ADMIN;
        } else if ("USER".equalsIgnoreCase(dto.getTipo())) {
            novaRole = Role.USER;
        } else {
            throw new ValidacaoException("Tipo de usuário inválido: " + dto.getTipo());
        }

        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail().toLowerCase().trim());
        existente.setSenha(passwordEncoder.encode(dto.getSenha()));
        existente.setRole(novaRole);
        existente.setUpdatedAt(LocalDateTime.now());

        if ("ONG".equalsIgnoreCase(dto.getTipo())) {
            Ong ong = ongRepository.findByUsuario_Id(id)
                    .orElseThrow(() -> new ValidacaoException("ONG não encontrada para este usuário."));
            ong.setCnpj(dto.getCnpj());
            ong.setDescricao(dto.getDescricao());
            ongRepository.save(ong);
        }

        return usuarioRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Usuario u = buscarPorId(id);

        if (u.getRole() == Role.ADMIN) {
            ongRepository.findByUsuario_Id(id).ifPresent(ongRepository::delete);
        }

        usuarioRepository.delete(u);
    }

    // PARTE DO PAINEL ADMIN
    public void alterarRole(Long id, Role novaRole) {
        Usuario usuario = buscarPorId(id);
        usuario.setRole(novaRole);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    // === MÉTODOS PARA RESET DE SENHA SIMPLIFICADO ===

    /**
     * Atualiza apenas a senha do usuário
     */
    @Transactional
    public void atualizarSenha(Long id, String novaSenhaHasheada) {
        Usuario usuario = buscarPorId(id);
        usuario.setSenha(novaSenhaHasheada);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    /**
     * Método auxiliar para verificar se um email existe no sistema
     */
    public boolean emailExiste(String email) {
        String emailNormalizado = email.toLowerCase().trim();
        return usuarioRepository.findByEmail(emailNormalizado).isPresent();
    }
}