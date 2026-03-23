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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seguidores",

    uniqueConstraints = @UniqueConstraint(columnNames = {"seguidor_id", "seguido_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Seguidor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Usuario seguidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seguido_id", nullable = false)
    private Usuario seguido;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
