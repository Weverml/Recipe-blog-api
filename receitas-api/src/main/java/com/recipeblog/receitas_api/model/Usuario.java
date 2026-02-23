package com.recipeblog.receitas_api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                         // diz ao JPA que essa classe é uma tabela
@Table(name = "usuarios")       // define o nome da tabela no banco
@Data                           // Lombok: gera getters, setters, toString automaticamente
@NoArgsConstructor              // Lombok: gera construtor vazio
@AllArgsConstructor             // Lombok: gera construtor com todos os campos
public class Usuario {
    
    @Id                                                    // chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // auto incremento (1, 2, 3...)
    private Long id;

    @NotBlank                          // validação: não pode ser vazio
    @Column(nullable = false)          // no banco: coluna obrigatória
    private String nome;

    @NotBlank
    @Email                             // validação: precisa ter formato de email
    @Column(nullable = false, unique = true)   // único no banco (não pode repetir)
    private String email;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataNascimento;
   
    @NotBlank
    @Column(nullable = false)
    private String senha;

    
    @Column(unique = true)             // username único, mas não obrigatório
    private String username;

    private String fotoPerfil;         // url da foto
    private String bio;                // descrição do perfil

    @Column(updatable = false)         // esse campo nunca será alterado após criado
    private LocalDateTime criadoEm = LocalDateTime.now(); // data de cadastro
}
