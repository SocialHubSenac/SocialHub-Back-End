package com.senac.socialhub.service;

import com.senac.socialhub.controller.dto.ComentarioRequestDTO;
import com.senac.socialhub.entity.Comentario;
import com.senac.socialhub.entity.Postagem;
import com.senac.socialhub.exception.ResourceNotFoundException;
import com.senac.socialhub.repository.ComentarioRepository;
import com.senac.socialhub.repository.PostagemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PostagemRepository postagemRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, PostagemRepository postagemRepository) {
        this.comentarioRepository = comentarioRepository;
        this.postagemRepository = postagemRepository;
    }

    public Comentario salvar(ComentarioRequestDTO dto) {
        Postagem postagem = postagemRepository.findById(dto.getPostagemId())
                .orElseThrow(() -> new ResourceNotFoundException("Postagem não encontrada."));

        Comentario novoComentario = Comentario.builder()
                .autor(dto.getAutor())
                .conteudo(dto.getConteudo())
                .dataCriacao(LocalDateTime.now())
                .postagem(postagem)
                .build();

        return comentarioRepository.save(novoComentario);
    }

    public List<Comentario> listar() {
        return comentarioRepository.findAll();
    }

    public Comentario buscarPorId(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado."));
    }

    public Comentario atualizar(Long id, ComentarioRequestDTO dto) {
        Comentario existente = buscarPorId(id);
        existente.setAutor(dto.getAutor());
        existente.setConteudo(dto.getConteudo());
        return comentarioRepository.save(existente);
    }

    public void excluir(Long id) {
        Comentario comentario = buscarPorId(id);
        comentarioRepository.delete(comentario);
    }

}
