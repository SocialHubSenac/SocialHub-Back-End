package com.senac.socialhub.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostagemResponseDTO {

    private Long id;
    private String titulo;
    private String conteudo;
    private LocalDateTime dataCriacao;
    private Long instituicaoId;
    private String instituicaoNome;
}
