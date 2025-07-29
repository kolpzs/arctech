package com.arctech.reports.dto;

import com.arctech.financial.entities.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExtratoDto {

    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Float totalReceitasPeriodo;
    private Float totalDespesasPeriodo;
    private Float saldoPeriodo;
    private List<Conta> transacoes;
}