package com.senac.socialhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instituicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String Nome;

    @NotBlank
    private String Descricao;

    @NotBlank
    private String Missao;

    @NotBlank
    private String Localizacao;

    private boolean semFinsLucrativos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getNome() {
        return Nome;
    }

    public void setNome(@NotBlank String nome) {
        Nome = nome;
    }

    public @NotBlank String getDescricao() {
        return Descricao;
    }

    public void setDescricao(@NotBlank String descricao) {
        Descricao = descricao;
    }

    public @NotBlank String getMissao() {
        return Missao;
    }

    public void setMissao(@NotBlank String missao) {
        Missao = missao;
    }

    public @NotBlank String getLocalizacao() {
        return Localizacao;
    }

    public void setLocalizacao(@NotBlank String localizacao) {
        Localizacao = localizacao;
    }

    public boolean isSemFinsLucrativos() {
        return semFinsLucrativos;
    }

    public void setSemFinsLucrativos(boolean semFinsLucrativos) {
        this.semFinsLucrativos = semFinsLucrativos;
    }
}
