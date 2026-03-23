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

    private String imagemUrl;

    private Integer tempoPreparo;

    private LocalDateTime criadoEm;

    private Long usuarioId;
    private String usuarioNome;
    private String usuarioUsername;
    private String usuarioFoto;
}
