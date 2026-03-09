package com.recipeblog.receitas_api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recipeblog.receitas_api.dto.ReceitaDTO; 
import com.recipeblog.receitas_api.service.ReceitaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/receitas")
@RequiredArgsConstructor
public class ReceitaController {
    // O Service é injetado aqui — o Controller apenas recebe a requisição e delega ao Service
    private final ReceitaService receitaService;

    // POST /receitas — Cria uma nova receita
    // consumes = "multipart/form-data" porque envia texto + arquivo de imagem juntos
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ReceitaDTO> criar(
            @RequestParam Long usuarioId,              // ID do usuário que está criando
            @RequestParam String titulo,               // Título da receita (obrigatório)
            @RequestParam(required = false) String descricao,       // Descrição (opcional)
            @RequestParam String ingredientes,         // Ingredientes (obrigatório)
            @RequestParam String modoPreparo,          // Modo de preparo (obrigatório)
            @RequestParam(required = false) String categoria,       // Categoria (opcional)
            @RequestParam(required = false) Integer tempoPreparo,   // Tempo em minutos (opcional)
            @RequestPart(required = false) MultipartFile imagem     // Arquivo de imagem (opcional)
    ) throws IOException {
        return ResponseEntity.ok(receitaService.criar(
                usuarioId, titulo, descricao, ingredientes,
                modoPreparo, categoria, tempoPreparo, imagem));
    }

    // GET /receitas/usuario/{usuarioId} — Lista todas as receitas de um usuário
    // Usado na página de perfil para montar o grid de publicações
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReceitaDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(receitaService.listarPorUsuario(usuarioId));
    }

    // GET /receitas/{id} — Busca uma receita específica pelo ID
    // Usado para abrir a página de detalhe de uma receita
    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(receitaService.buscarPorId(id));
    }
}
