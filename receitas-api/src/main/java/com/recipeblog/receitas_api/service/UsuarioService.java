package com.recipeblog.receitas_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipeblog.receitas_api.dto.UsuarioDTO;
import com.recipeblog.receitas_api.exception.BusinessException;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.UsuarioRepository;

@Service // diz ao Spring que essa classe é um Service e deve ser gerenciada por ele
public class UsuarioService {
     @Autowired   // o Spring injeta o Repository automaticamente (injeção de dependência)
    private UsuarioRepository repository;

    public Usuario cadastrar(UsuarioDTO dto) {
        // verifica se já existe um usuário com esse email antes de salvar
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }
        if (repository.existsByUsername(dto.getUsername())) {
            throw new BusinessException("Username já cadastrado.");
        }

        // cria um novo Usuario a partir dos dados que vieram do DTO
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setSenha(dto.getSenha());
        usuario.setUsername(dto.getUsername());
        usuario.setBio(dto.getBio());
        usuario.setFotoPerfil(dto.getFotoPerfil());

        return repository.save(usuario);   // salva no banco e retorna o usuario salvo
    }

    public Optional<Usuario> login(String email, String senha) {
    if (!repository.existsByEmail(email)) {
        throw new ResourceNotFoundException("Email não encontrado.");
    }
    return repository.findByEmail(email)
            .filter(u -> u.getSenha().equals(senha));
}
    
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);   // findById já vem de graça no JpaRepository
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        // busca o usuario, se não existir lança um erro
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // atualiza apenas os campos que fazem sentido alterar
        // email e senha não são atualizados aqui por segurança
        usuario.setNome(dto.getNome());
        usuario.setBio(dto.getBio());
        usuario.setFotoPerfil(dto.getFotoPerfil());
        usuario.setUsername(dto.getUsername());

        return repository.save(usuario);   // salva as alterações no banco
    }
}
