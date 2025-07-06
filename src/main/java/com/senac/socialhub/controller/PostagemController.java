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
@RequestMapping("/postagens")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // Suporte para ambos os frontends
public class PostagemController {

    private final PostagemService postagemService;

    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    @PostMapping
    public ResponseEntity<PostagemResponseDTO> criar(@RequestBody @Valid PostagemRequestDTO dto) {
        try {
            Postagem nova = postagemService.salvar(dto);
            return ResponseEntity.ok(toResponseDTO(nova));
        } catch (Exception e) {
            System.err.println("Erro ao criar postagem: " + e.getMessage());
            throw e; // Re-lança para o GlobalExceptionHandler capturar
        }
    }

    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> listar() {
        try {
            List<Postagem> postagens = postagemService.listar();
            List<PostagemResponseDTO> resposta = postagens.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro ao listar postagens: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/home")
    public ResponseEntity<List<PostagemResponseDTO>> listarParaHome() {
        try {
            List<Postagem> postagens = postagemService.listarParaHome();
            List<PostagemResponseDTO> resposta = postagens.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro ao listar postagens para home: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/instituicao/{instituicaoId}")
    public ResponseEntity<List<PostagemResponseDTO>> listarPorInstituicao(@PathVariable Long instituicaoId) {
        try {
            List<Postagem> postagens = postagemService.listarPorInstituicao(instituicaoId);
            List<PostagemResponseDTO> resposta = postagens.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro ao listar postagens por instituição: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PostagemResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Postagem> postagens = postagemService.listarPorUsuario(usuarioId);
            List<PostagemResponseDTO> resposta = postagens.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro ao listar postagens por usuário: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<PostagemResponseDTO>> buscarPorTitulo(@RequestParam String titulo) {
        try {
            List<Postagem> postagens = postagemService.buscarPorTitulo(titulo);
            List<PostagemResponseDTO> resposta = postagens.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro ao buscar postagens por título: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/buscar/conteudo")
    public ResponseEntity<List<PostagemResponseDTO>> buscarPorConteudo(@RequestParam String conteudo) {
        try {
            List<Postagem> postagens = postagemService.buscarPorConteudo(conteudo);
            List<PostagemResponseDTO> resposta = postagens.stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            System.err.println("Erro ao buscar postagens por conteúdo: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Postagem postagem = postagemService.buscarPorId(id);
            return ResponseEntity.ok(toResponseDTO(postagem));
        } catch (Exception e) {
            System.err.println("Erro ao buscar postagem por ID: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PostagemRequestDTO dto) {
        try {
            Postagem atualizado = postagemService.atualizar(id, dto);
            return ResponseEntity.ok(toResponseDTO(atualizado));
        } catch (Exception e) {
            System.err.println("Erro ao atualizar postagem: " + e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            postagemService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Erro ao excluir postagem: " + e.getMessage());
            throw e;
        }
    }

    private PostagemResponseDTO toResponseDTO(Postagem p) {
        return new PostagemResponseDTO(
                p.getId(),
                p.getTitulo(),
                p.getConteudo(),
                p.getDataCriacao(),
                p.getInstituicao().getId(),
                p.getInstituicao().getNome(),
                p.getUsuario().getId(),
                p.getUsuario().getNome()
        );
    }
}