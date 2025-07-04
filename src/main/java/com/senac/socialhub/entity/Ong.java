package com.senac.socialhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ongs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String descricao;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
