package com.arctech.dto;

import com.arctech.entities.Conta;
import com.arctech.entities.StatusConta; // Importe o novo Enum
import com.arctech.entities.Tipo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate; // Use LocalDate
import java.time.LocalDateTime;

@Getter
@Setter
public class ContaResponseDto {

    private Long id;
    private String descricao;
    private Float valor;
    private Tipo tipo;

    private StatusConta status;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    private String categoriaNome;
    private String instituicaoNome;
    private LocalDateTime dataCriacao;
    private String criadoPor;

    public ContaResponseDto(Conta conta) {
        this.id = conta.getId();
        this.descricao = conta.getDescricao();
        this.valor = conta.getValor();
        this.tipo = conta.getTipo();

        this.status = conta.getStatus();
        this.dataVencimento = conta.getDataVencimento();
        this.dataPagamento = conta.getDataPagamento();

        this.dataCriacao = conta.getDataCriacao();
        this.criadoPor = conta.getCriadoPor();

        if (conta.getCategoria() != null) {
            this.categoriaNome = conta.getCategoria().getNome();
        }
        if (conta.getInstituicao() != null) {
            this.instituicaoNome = conta.getInstituicao().getNome();
        }
    }
}