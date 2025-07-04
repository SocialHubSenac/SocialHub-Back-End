package com.senac.socialhub.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String tipo;

    private String cnpj;
    private String descricao;

    public UsuarioResponseDTO(Long id, String nome, String email, String name) {
    }
}
