package com.recipeblog.receitas_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class ReceitaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String ingredientes;
    private String modoPreparo;
    private String categoria;

    // URL completa da imagem (ex: "/uploads/abc123.jpg") — pronta para o frontend usar
    private String imagemUrl;

    // Tempo de preparo em minutos
    private Integer tempoPreparo;

    // Data de criação da receita
    private LocalDateTime criadoEm;

    // Dados básicos do usuário dono da receita
    // Incluímos aqui para o frontend não precisar fazer uma segunda requisição
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioUsername;
}
