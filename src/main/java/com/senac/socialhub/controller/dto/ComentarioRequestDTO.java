package com.senac.socialhub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioRequestDTO {

    @NotBlank(message = "O autor é obrigatório.")
    private String autor;

    @NotBlank(message = "O conteúdo é obrigatório.")
    private String conteudo;

    @NotNull(message = "O ID da postagem é obrigatório.")
    private Long postagemId;
}
