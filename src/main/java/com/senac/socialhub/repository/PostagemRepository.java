package com.senac.socialhub.repository;

import com.senac.socialhub.entity.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostagemRepository extends  JpaRepository<Postagem, Long>{

}
