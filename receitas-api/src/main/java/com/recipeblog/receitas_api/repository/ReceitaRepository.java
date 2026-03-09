package com.recipeblog.receitas_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipeblog.receitas_api.model.Receita;

// Repository é a camada que conversa diretamente com o banco de dados
// Ao estender JpaRepository, ganhamos automaticamente métodos prontos como:
// save(), findById(), findAll(), delete(), etc.
public interface ReceitaRepository extends JpaRepository<Receita, Long>{
    // O Spring Data JPA consegue criar a query SQL automaticamente pelo nome do método
    // Tradução: SELECT * FROM receitas WHERE usuario_id = ? ORDER BY criado_em DESC
    // Ou seja: busca todas as receitas de um usuário, da mais recente para a mais antiga
    List<Receita> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

    // Conta quantas receitas um usuário tem
    // Tradução: SELECT COUNT(*) FROM receitas WHERE usuario_id = ?
    // Usado para exibir o número de publicações no perfil
    long countByUsuarioId(Long usuarioId);
}
