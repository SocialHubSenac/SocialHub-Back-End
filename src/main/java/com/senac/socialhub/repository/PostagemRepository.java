package com.senac.socialhub.repository;

import com.senac.socialhub.entity.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {


    List<Postagem> findAllByOrderByDataCriacaoDesc();
    List<Postagem> findTop10ByOrderByDataCriacaoDesc();
    List<Postagem> findByInstituicaoIdOrderByDataCriacaoDesc(Long instituicaoId);
    List<Postagem> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    @Query("SELECT p FROM Postagem p WHERE p.titulo LIKE %:titulo% ORDER BY p.dataCriacao DESC")
    List<Postagem> findByTituloContainingIgnoreCaseOrderByDataCriacaoDesc(@Param("titulo") String titulo);

    @Query("SELECT p FROM Postagem p WHERE p.conteudo LIKE %:conteudo% ORDER BY p.dataCriacao DESC")
    List<Postagem> findByConteudoContainingIgnoreCaseOrderByDataCriacaoDesc(@Param("conteudo") String conteudo);
}