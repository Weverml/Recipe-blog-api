package com.recipeblog.receitas_api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // não inclui campos nulos no JSON
public class UsuarioDTO {
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String senha;
    private String username;
    private String bio;
    private String fotoPerfil;
}
