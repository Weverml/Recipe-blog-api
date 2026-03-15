package com.recipeblog.receitas_api.dto;

import java.time.LocalDate;

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
public class PerfilDTO {
    private Long id;
    private String nome;
    private String username;
    private String email;
    private String bio;
    private String fotoPerfil;
    private LocalDate dataNascimento;
    private long totalReceitas;
}
