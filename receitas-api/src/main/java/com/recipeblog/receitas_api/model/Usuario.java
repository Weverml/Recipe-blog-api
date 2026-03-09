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

@Entity                         
@Table(name = "usuarios")      
@Data                          
@NoArgsConstructor              
@AllArgsConstructor             
public class Usuario {
    
    @Id                                                    
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long id;

    @NotBlank                          
    @Column(nullable = false)          
    private String nome;

    @NotBlank
    @Email                             
    @Column(nullable = false, unique = true)   
    private String email;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataNascimento;
   
    @NotBlank
    @Column(nullable = false)
    private String senha;

    
    @Column(unique = true)            
    private String username;

    private String fotoPerfil;         
    private String bio;                

    @Column(updatable = false)         
    private LocalDateTime criadoEm = LocalDateTime.now(); 
}
