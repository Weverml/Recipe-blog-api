package com.recipeblog.receitas_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recipeblog.receitas_api.model.Receita;

public interface ReceitaRepository extends JpaRepository<Receita, Long>{
    
    List<Receita> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);
    long countByUsuarioId(Long usuarioId);
    @Query("SELECT r FROM Receita r WHERE r.usuario.id IN :ids ORDER BY r.criadoEm DESC")
    List<Receita> findByUsuarioIdInOrderByCriadoEmDesc(@Param("ids") List<Long> ids);
}
