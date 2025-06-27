package com.senac.socialhub.service;

import com.senac.socialhub.controller.dto.PostagemRequestDTO;
import com.senac.socialhub.entity.Instituicao;
import com.senac.socialhub.entity.Postagem;
import com.senac.socialhub.exception.ResourceNotFoundException;
import com.senac.socialhub.repository.InstituicaoRepository;
import com.senac.socialhub.repository.PostagemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final InstituicaoRepository instituicaoRepository;

    public PostagemService(PostagemRepository postagemRepository, InstituicaoRepository instituicaoRepository){
        this.postagemRepository = postagemRepository;
        this.instituicaoRepository = instituicaoRepository;
    }

    public Postagem salvar(PostagemRequestDTO dto) {
        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada."));

        Postagem nova = Postagem.builder()
                .titulo(dto.getTitulo())
                .conteudo(dto.getConteudo())
                .dataCriacao(LocalDateTime.now())
                .instituicao(instituicao)
                .build();

        return postagemRepository.save(nova);
    }

    public List<Postagem> listar() {
        return postagemRepository.findAll();
    }

    public Postagem buscarPorId(Long id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postagem não encontrada."));
    }

    public Postagem atualizar(Long id, PostagemRequestDTO dto) {
        Postagem existente = buscarPorId(id);
        existente.setTitulo(dto.getTitulo());
        existente.setConteudo(dto.getConteudo());
        return postagemRepository.save(existente);
    }

    public void excluir(Long id) {
        Postagem postagem = buscarPorId(id);
        postagemRepository.delete(postagem);
}
}
