package com.recipeblog.receitas_api.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.recipeblog.receitas_api.dto.PerfilDTO;
import com.recipeblog.receitas_api.dto.UsuarioDTO;
import com.recipeblog.receitas_api.exception.BusinessException;
import com.recipeblog.receitas_api.exception.ResourceNotFoundException;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.repository.ReceitaRepository;
import com.recipeblog.receitas_api.repository.UsuarioRepository;

@Service 
public class UsuarioService {
     @Autowired
    private UsuarioRepository repository;
 
    @Autowired
    private ReceitaRepository receitaRepository;
 
    @Autowired
    private Cloudinary cloudinary;
 
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
 
    public Usuario atualizarPerfil(Long id, String nome, String bio, MultipartFile foto) throws IOException {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
 
        if (nome != null) {
            usuario.setNome(nome);
        }
 
        if (bio != null) {
            usuario.setBio(bio);
        }

        if (foto != null && !foto.isEmpty()) {
            String fotoUrl = uploadParaCloudinary(foto);
            usuario.setFotoPerfil(fotoUrl);
        }
 
        return repository.save(usuario);
    }
 

    public PerfilDTO perfilCompleto(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
 
        long totalReceitas = receitaRepository.countByUsuarioId(id);
 
        return PerfilDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .bio(usuario.getBio())
                .fotoPerfil(usuario.getFotoPerfil())
                .dataNascimento(usuario.getDataNascimento())
                .totalReceitas(totalReceitas)
                .build();
    }
 
    private String uploadParaCloudinary(MultipartFile arquivo) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                arquivo.getBytes(),
                ObjectUtils.asMap("folder", "perfis") 
        );
        return (String) uploadResult.get("secure_url");
    }
}
