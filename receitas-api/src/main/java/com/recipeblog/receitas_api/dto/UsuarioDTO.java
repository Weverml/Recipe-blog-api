package com.recipeblog.receitas_api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String senha;
    private String username;
    private String bio;
    private String fotoPerfil;
}
