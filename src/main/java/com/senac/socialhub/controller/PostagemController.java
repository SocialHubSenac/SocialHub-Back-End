package com.senac.socialhub.controller;

import com.senac.socialhub.controller.dto.PostagemRequestDTO;
import com.senac.socialhub.controller.dto.PostagemResponseDTO;
import com.senac.socialhub.entity.Postagem;
import com.senac.socialhub.service.PostagemService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    private final PostagemService postagemService;

    private PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @PostMapping
    public ResponseEntity<PostagemResponseDTO> criar(@RequestBody @Valid PostagemRequestDTO dto) {
        Postagem nova = postagemService.salvar(dto);
        return ResponseEntity.ok(toResponseDTO(nova));
    }

    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> listar() {
        List<Postagem> postagens = postagemService.listar();
        List<PostagemResponseDTO> resposta = postagens.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> buscarPorId(@PathVariable Long id) {
        Postagem postagem = postagemService.buscarPorId(id);
        return ResponseEntity.ok(toResponseDTO(postagem));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PostagemRequestDTO dto) {
        Postagem atualizado = postagemService.atualizar(id, dto);
        return ResponseEntity.ok(toResponseDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        postagemService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private PostagemResponseDTO toResponseDTO(Postagem p) {
        return new PostagemResponseDTO(
                p.getId(),
                p.getTitulo(),
                p.getConteudo(),
                p.getDataCriacao(),
                p.getInstituicao().getId(),
                p.getInstituicao().getNome()
        );
    }

}
