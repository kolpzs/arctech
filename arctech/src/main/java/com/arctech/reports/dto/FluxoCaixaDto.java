package com.arctech.reports.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class FluxoCaixaDto {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Float saldoInicialPeriodo;
    private Float saldoFinalPeriodo;
    private Float totalReceitasPeriodo;
    private Float totalDespesasPeriodo;
    private List<FluxoCaixaDiario> fluxoDiario;

    @Getter
    @Setter
    @Builder
    public static class FluxoCaixaDiario {
        private LocalDate data;
        private Float totalReceitas;
        private Float totalDespesas;
        private Float saldoDoDia;
    }
}