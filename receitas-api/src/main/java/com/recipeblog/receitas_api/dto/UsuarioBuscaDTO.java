package com.recipeblog.receitas_api.dto;

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
public class UsuarioBuscaDTO {
    
    private Long id;
    private String nome;
    private String username;
    private String bio;
    private String fotoPerfil;
}
