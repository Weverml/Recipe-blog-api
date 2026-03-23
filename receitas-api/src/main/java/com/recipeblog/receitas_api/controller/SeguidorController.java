package com.recipeblog.receitas_api.controller;

import com.recipeblog.receitas_api.service.SeguidorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class SeguidorController {
    
    private final SeguidorService seguidorService;

    @PostMapping("/{id}/seguir/{seguidoId}")
    public ResponseEntity<Void> seguir(@PathVariable Long id, @PathVariable Long seguidoId) {
        seguidorService.seguir(id, seguidoId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/seguir/{seguidoId}")
    public ResponseEntity<Void> deixarDeSeguir(@PathVariable Long id, @PathVariable Long seguidoId) {
        seguidorService.deixarDeSeguir(id, seguidoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/seguindo/{seguidoId}")
    public ResponseEntity<Boolean> estaSeguindo(@PathVariable Long id, @PathVariable Long seguidoId) {
        return ResponseEntity.ok(seguidorService.estaSeguindo(id, seguidoId));
    }
}
