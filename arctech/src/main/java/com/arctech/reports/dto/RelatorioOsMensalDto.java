package com.arctech.reports.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RelatorioOsMensalDto {
    private int ano;
    private int mes;
    private long quantidadeOsFinalizadas;
    private Double receitaTotalOs;
    private Double custoTotalOs;
    private Double lucroTotalOs;
    private List<LucroPorOsDto> detalhamento;
}