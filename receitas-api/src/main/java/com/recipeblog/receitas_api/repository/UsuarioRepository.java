package com.recipeblog.receitas_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipeblog.receitas_api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
