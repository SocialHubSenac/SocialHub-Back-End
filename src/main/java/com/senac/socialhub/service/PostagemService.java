package com.senac.socialhub.service;

import com.senac.socialhub.controller.dto.PostagemRequestDTO;
import com.senac.socialhub.entity.Instituicao;
import com.senac.socialhub.entity.Postagem;
import com.senac.socialhub.entity.Usuario;
import com.senac.socialhub.exception.ResourceNotFoundException;
import com.senac.socialhub.repository.InstituicaoRepository;
import com.senac.socialhub.repository.PostagemRepository;
import com.senac.socialhub.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final InstituicaoRepository instituicaoRepository;
    private final UsuarioRepository usuarioRepository;

    public PostagemService(PostagemRepository postagemRepository,
                           InstituicaoRepository instituicaoRepository,
                           UsuarioRepository usuarioRepository) {
        this.postagemRepository = postagemRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Postagem salvar(PostagemRequestDTO dto) {
        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Postagem nova = Postagem.builder()
                .titulo(dto.getTitulo())
                .conteudo(dto.getConteudo())
                .dataCriacao(LocalDateTime.now())
                .instituicao(instituicao)
                .usuario(usuario)
                .build();

        return postagemRepository.save(nova);
    }

    public List<Postagem> listar() {
        return postagemRepository.findAllByOrderByDataCriacaoDesc();
    }

    public List<Postagem> listarParaHome() {
        return postagemRepository.findTop10ByOrderByDataCriacaoDesc();
    }

    public List<Postagem> listarPorInstituicao(Long instituicaoId) {
        return postagemRepository.findByInstituicaoIdOrderByDataCriacaoDesc(instituicaoId);
    }

    public List<Postagem> listarPorUsuario(Long usuarioId) {
        return postagemRepository.findByUsuarioIdOrderByDataCriacaoDesc(usuarioId);
    }

    public List<Postagem> buscarPorTitulo(String titulo) {
        return postagemRepository.findByTituloContainingIgnoreCaseOrderByDataCriacaoDesc(titulo);
    }

    public List<Postagem> buscarPorConteudo(String conteudo) {
        return postagemRepository.findByConteudoContainingIgnoreCaseOrderByDataCriacaoDesc(conteudo);
    }

    public Postagem buscarPorId(Long id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postagem não encontrada."));
    }

    @Transactional
    public Postagem atualizar(Long id, PostagemRequestDTO dto) {
        Postagem existente = buscarPorId(id);

        if (!existente.getInstituicao().getId().equals(dto.getInstituicaoId())) {
            Instituicao novaInstituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));
            existente.setInstituicao(novaInstituicao);
        }

        existente.setTitulo(dto.getTitulo());
        existente.setConteudo(dto.getConteudo());

        return postagemRepository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Postagem postagem = buscarPorId(id);
        postagemRepository.delete(postagem);
    }
}