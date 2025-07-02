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
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidacaoException("Já existe um usuário com este email.");
        }

        if ("ONG".equalsIgnoreCase(dto.getTipo())) {
            if (dto.getCnpj() == null || dto.getCnpj().isEmpty()) {
                throw new ValidacaoException("CNPJ é obrigatório para ONG.");
            }
            if (ongRepository.existsByCnpj(dto.getCnpj())) {
                throw new ValidacaoException("Já existe uma ONG cadastrada com este CNPJ.");
            }
            if (dto.getDescricao() == null || dto.getDescricao().isEmpty()) {
                throw new ValidacaoException("Descrição é obrigatória para ONG.");
            }
        }

        Usuario novoUsuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role("ONG".equalsIgnoreCase(dto.getTipo()) ? Role.ADMIN : Role.USER)
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        if ("ONG".equalsIgnoreCase(dto.getTipo())) {
            Ong ong = Ong.builder()
                    .cnpj(dto.getCnpj())
                    .descricao(dto.getDescricao())
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

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario existente = buscarPorId(id);
        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setSenha(passwordEncoder.encode(dto.getSenha()));
        existente.setRole(Role.valueOf(dto.getTipo()));

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
}
