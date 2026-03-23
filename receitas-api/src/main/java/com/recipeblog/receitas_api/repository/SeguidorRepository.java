package com.recipeblog.receitas_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipeblog.receitas_api.model.Seguidor;

public interface SeguidorRepository extends JpaRepository<Seguidor, Long>{
 
    List<Seguidor> findBySeguidorId(Long seguidorId);
    List<Seguidor> findBySeguidoId(Long seguidoId);
    boolean existsBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);
    long countBySeguidoId(Long seguidoId);
    long countBySeguidorId(Long seguidorId);
    Optional<Seguidor> findBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);
}
