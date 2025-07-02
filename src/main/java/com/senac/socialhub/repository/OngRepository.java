package com.senac.socialhub.repository;

import com.senac.socialhub.entity.Ong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OngRepository extends JpaRepository<Ong, Long> {
    boolean existsByCnpj(String cnpj);

    Optional<Ong> findByUsuario_Id(Long usuarioId);
}

