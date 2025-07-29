package com.arctech.reports.dto;

import com.arctech.financial.dto.ContaResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DashboardDto {

    private Float saldoAtual;
    private Float totalReceitasMes;
    private Float totalDespesasMes;
    private List<ContaResponseDto> proximasContasVencer;
    private List<ContaResponseDto> contasPagasMes;
    private List<ContaResponseDto> contasPendentesMes;
    private Long osFinalizadasMes;
    private Double lucroOsMes;
}