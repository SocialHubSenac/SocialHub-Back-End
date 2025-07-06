package com.senac.socialhub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostagemRequestDTO {

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotBlank(message = "O conteúdo é obrigatório.")
    private String conteudo;

    @NotNull(message = "O ID da instituição é obrigatório.")
    private Long instituicaoId;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Long usuarioId;
}
