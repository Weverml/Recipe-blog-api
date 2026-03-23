package com.recipeblog.receitas_api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recipeblog.receitas_api.dto.PerfilDTO;
import com.recipeblog.receitas_api.dto.UsuarioBuscaDTO;
import com.recipeblog.receitas_api.dto.UsuarioDTO;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.service.UsuarioService;


@RestController          
@RequestMapping("/usuarios")   
public class UsuarioController {
    
     @Autowired
    private UsuarioService service;

    @PostMapping           
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.cadastrar(dto));
    }

    @PostMapping("/login")   
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return service.login(body.get("email"), body.get("senha"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/{id}")   
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")   
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
    
    @GetMapping("/{id}/perfil")
    public ResponseEntity<PerfilDTO> perfilCompleto(@PathVariable Long id) {
        return ResponseEntity.ok(service.perfilCompleto(id));
    }

    @PutMapping(value = "/{id}/perfil", consumes = "multipart/form-data")
    public ResponseEntity<Usuario> atualizarPerfil(
            @PathVariable Long id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String bio,
            @RequestPart(required = false) MultipartFile foto
    ) throws IOException {
        return ResponseEntity.ok(service.atualizarPerfil(id, nome, bio, foto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<UsuarioBuscaDTO>> buscar(@RequestParam String termo) {
        return ResponseEntity.ok(
            service.buscarPorNome(termo).stream()
                .map(u -> UsuarioBuscaDTO.builder()
                    .id(u.getId())
                    .nome(u.getNome())
                    .username(u.getUsername())
                    .bio(u.getBio())
                    .fotoPerfil(u.getFotoPerfil())
                    .build()
                )
                .collect(java.util.stream.Collectors.toList())
        );
    }
}
