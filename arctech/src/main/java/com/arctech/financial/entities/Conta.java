package com.arctech.financial.entities;

import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.enums.Tipo;
import com.arctech.users.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contas")
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConta status;

    @NotBlank(message = "A descrição não pode ser vazia.")
    @Column(nullable = false)
    private String descricao;

    @Min(value = 0)
    @Column(nullable = false)
    private Float valor;

    @NotNull(message = "O tipo (RECEITA, DESPESA) não pode ser nulo.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_recorrente_id")
    private ContaRecorrente contaRecorrente;

    @NotNull(message = "A instituição não pode ser nula.")
    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime dataModificacao;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String criadoPor;

    @LastModifiedBy
    @Column(insertable = false)
    private String modificadoPor;
}
