package com.arctech.reports.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DreDto {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Float receitaBruta;
    private Float custos;
    private Float lucroBruto;
    private Float despesasOperacionais;
    private Float lucroLiquido;
}