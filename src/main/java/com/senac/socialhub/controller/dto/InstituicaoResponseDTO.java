package com.senac.socialhub.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstituicaoResponseDTO {
    private Long id;
    private String Nome;
    private String Descricao;
    private String Missao;
    private String Localizacao;
    private boolean semFinsLucrativos;
}
