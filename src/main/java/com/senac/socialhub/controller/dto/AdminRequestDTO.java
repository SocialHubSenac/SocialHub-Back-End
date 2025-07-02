package com.senac.socialhub.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminRequestDTO {
        @NotBlank(message = "O novo tipo é obrigatório (USER ou ADMIN).")
        private String novaRole;
}
