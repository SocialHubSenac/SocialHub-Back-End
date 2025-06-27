package com.senac.socialhub.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ComentarioResponseDTO {

    private Long id;
    private String autor;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private Long postagemId;
    private String postagemTitulo;
}
