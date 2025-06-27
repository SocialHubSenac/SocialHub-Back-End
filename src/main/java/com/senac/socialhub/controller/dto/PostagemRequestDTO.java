package com.senac.socialhub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostagemRequestDTO {

    @NotBlank(message = "O titulo é obrigatorio.")
    private String titulo;

    @NotBlank(message = "O conteudo é obrigatorio.")
    private String conteudo;

    @NotBlank(message = "O ID da instituição é obrigatório.")
    private Long instituicaoId;

}
