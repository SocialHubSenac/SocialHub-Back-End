package com.senac.socialhub.controller;

import com.senac.socialhub.controller.dto.InstituicaoRequestDTO;
import com.senac.socialhub.controller.dto.InstituicaoResponseDTO;
import com.senac.socialhub.entity.Instituicao;
import com.senac.socialhub.service.InstituicaoService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @PostMapping
    public ResponseEntity<InstituicaoResponseDTO> criar(@RequestBody @Valid InstituicaoRequestDTO dto) {
        Instituicao nova = instituicaoService.salvar(dto);
        return ResponseEntity.ok(toResponseDTO(nova));
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoResponseDTO>> listar() {
        List<Instituicao> lista = instituicaoService.listar();
        List<InstituicaoResponseDTO> resposta = lista.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoResponseDTO> buscarPorId(@PathVariable Long id) {
        Instituicao i = instituicaoService.buscarPorId(id);
        return ResponseEntity.ok(toResponseDTO(i));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstituicaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid InstituicaoRequestDTO dto) {
        Instituicao atualizada = instituicaoService.atualizar(id, dto);
        return ResponseEntity.ok(toResponseDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        instituicaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private InstituicaoResponseDTO toResponseDTO(Instituicao i) {
        return new InstituicaoResponseDTO(
                i.getId(),
                i.getNome(),
                i.getDescricao(),
                i.getMissao(),
                i.getLocalizacao(),
                i.isSemFinsLucrativos()
        );
    }
}
