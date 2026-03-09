package com.recipeblog.receitas_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Entity diz ao Spring que essa classe representa uma tabela no banco de dados
// @Table define o nome da tabela como "receitas"
@Entity
@Table(name = "receitas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receita {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable = false significa que esse campo é obrigatório no banco
    @Column(nullable = false)
    private String titulo;

    // TEXT permite textos longos no banco (diferente de VARCHAR que tem limite curto)
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String ingredientes;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String modoPreparo;

    // Categoria da receita (ex: "Doces", "Massas", "Fitness") — opcional
    private String categoria;

    // Guarda apenas o nome do arquivo (ex: "abc123.jpg")
    // A URL completa (/uploads/abc123.jpg) é montada no Service
    private String imagemUrl;

    // Tempo de preparo em minutos (ex: 45 = 45 minutos)
    private Integer tempoPreparo;

    // Data e hora de quando a receita foi criada
    // updatable = false impede que esse valor seja alterado depois de salvo
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    // Relacionamento: muitas receitas pertencem a um único usuário
    // FetchType.LAZY = só carrega os dados do usuário quando necessário (melhor desempenho)
    // JoinColumn define o nome da coluna que liga essa tabela à tabela de usuários
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
