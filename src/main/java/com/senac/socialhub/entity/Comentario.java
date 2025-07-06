package com.senac.socialhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String autor;

    private String conteudo;

    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "postagem_id", nullable = false)
    private Postagem postagem;
}
