package com.senac.socialhub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstituicaoRequestDTO {

    @NotBlank(message = "Nome é obrigatório. ")
    private String Nome;

    @NotBlank(message = "Descrição é obrigatória. ")
    private String Descricao;

    @NotBlank(message = "Missão é obrigatória. ")
    private String Missao;

    @NotBlank(message = "Localização é obrigatória. ")
    private String Localizacao;

    private boolean semFinsLucrativos;
}