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
    private final ReceitaService receitaService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ReceitaDTO> criar(
            @RequestParam Long usuarioId,              
            @RequestParam String titulo,               
            @RequestParam(required = false) String descricao,       
            @RequestParam String ingredientes,         
            @RequestParam String modoPreparo,          
            @RequestParam(required = false) String categoria,       
            @RequestParam(required = false) Integer tempoPreparo,   
            @RequestPart(required = false) MultipartFile imagem     
    ) throws IOException {
        return ResponseEntity.ok(receitaService.criar(
                usuarioId, titulo, descricao, ingredientes,
                modoPreparo, categoria, tempoPreparo, imagem));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReceitaDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(receitaService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(receitaService.buscarPorId(id));
    }

    @GetMapping("/feed/{usuarioId}")
    public ResponseEntity<List<ReceitaDTO>> feed(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(receitaService.feed(usuarioId));
    }
}
