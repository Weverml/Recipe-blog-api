package com.recipeblog.receitas_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipeblog.receitas_api.dto.UsuarioDTO;
import com.recipeblog.receitas_api.model.Usuario;
import com.recipeblog.receitas_api.service.UsuarioService;


@RestController          // diz ao Spring que essa classe é um Controller REST
@RequestMapping("/usuarios")   // todas as rotas dessa classe começam com /usuarios
public class UsuarioController {
    
     @Autowired
    private UsuarioService service;

    @PostMapping           // POST /usuarios — cadastrar um novo usuário
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.cadastrar(dto));
    }

    @PostMapping("/login")   // POST /usuarios/login — fazer login
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return service.login(body.get("email"), body.get("senha"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/{id}")   // GET /usuarios/1 — buscar usuário por id
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")   // PUT /usuarios/1 — atualizar usuário
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
}
