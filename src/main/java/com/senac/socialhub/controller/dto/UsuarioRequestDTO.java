package com.senac.socialhub.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @Email(message = "Email inválido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    @NotBlank(message = "O tipo de usuário é obrigatório. (USER ou ONG)")
    private String tipo;

    private String cnpj;
    private String descricao;
}