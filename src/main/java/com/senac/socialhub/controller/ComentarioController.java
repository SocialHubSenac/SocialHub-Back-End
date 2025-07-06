package com.senac.socialhub.controller;

import com.senac.socialhub.controller.dto.ComentarioRequestDTO;
import com.senac.socialhub.controller.dto.ComentarioResponseDTO;
import com.senac.socialhub.entity.Comentario;
import com.senac.socialhub.service.ComentarioService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping
    public ResponseEntity<Comentario> criar(@RequestBody @Valid ComentarioRequestDTO dto) {
        Comentario novoComentario = comentarioService.salvar(dto);
        return ResponseEntity.ok(novoComentario);
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listar() {
        List<ComentarioResponseDTO> resposta = comentarioService.listar().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Comentario comentario = comentarioService.buscarPorId(id);
        return ResponseEntity.ok(toResponseDTO(comentario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComentarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ComentarioRequestDTO dto) {
        Comentario atualizado = comentarioService.atualizar(id, dto);
        return ResponseEntity.ok(toResponseDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        comentarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private ComentarioResponseDTO toResponseDTO(Comentario comentario) {
        return new ComentarioResponseDTO(
                comentario.getId(),
                comentario.getAutor(),
                comentario.getConteudo(),
                comentario.getDataCriacao(),
                comentario.getPostagem().getId(),
                comentario.getPostagem().getTitulo() // <- este campo estava faltando
        );
    }
}
