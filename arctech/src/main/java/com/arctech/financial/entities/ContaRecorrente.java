package com.arctech.financial.entities;

import com.arctech.financial.enums.Tipo;
import com.arctech.users.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ContaRecorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Float valor;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private Instituicao instituicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Frequencia frequencia; // Enum: MENSAL, SEMANAL, ANUAL

    private int diaDoVencimento; // Ex: 20 (para todo dia 20)

    private LocalDate dataInicio;
    private LocalDate dataFim; // A recorrência é válida até esta data

    public enum Frequencia {
        MENSAL, SEMANAL, BIMESTRAL, TRIMESTRAL, SEMESTRAL, ANUAL
    }
}