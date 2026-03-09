package com.recipeblog.receitas_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipeblog.receitas_api.dto.UsuarioDTO;
import com.recipeblog.receitas_api.exception.BusinessException;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.UsuarioRepository;

@Service 
public class UsuarioService {
     @Autowired   
    private UsuarioRepository repository;

    public Usuario cadastrar(UsuarioDTO dto) {
        
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }
        if (repository.existsByUsername(dto.getUsername())) {
            throw new BusinessException("Username já cadastrado.");
        }

        
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setSenha(dto.getSenha());
        usuario.setUsername(dto.getUsername());
        usuario.setBio(dto.getBio());
        usuario.setFotoPerfil(dto.getFotoPerfil());

        return repository.save(usuario);   
    }

    public Optional<Usuario> login(String email, String senha) {
    if (!repository.existsByEmail(email)) {
        throw new ResourceNotFoundException("Email não encontrado.");
    }
    return repository.findByEmail(email)
            .filter(u -> u.getSenha().equals(senha));
}
    
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);  
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        usuario.setNome(dto.getNome());
        usuario.setBio(dto.getBio());
        usuario.setFotoPerfil(dto.getFotoPerfil());
        usuario.setUsername(dto.getUsername());

        return repository.save(usuario);   
    }
}
