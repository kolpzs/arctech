package com.arctech.reports.services;

import com.arctech.financial.repositories.ContaRepository;
import com.arctech.os.repositories.OrdemServicoRepository;
import com.arctech.reports.dto.LucroPorOsDto;
import com.arctech.reports.dto.RelatorioOsMensalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class RelatorioOsService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private OrdemServicoRepository osRepository;

    @Transactional(readOnly = true)
    public RelatorioOsMensalDto gerarRelatorioMensal(int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate inicioMes = yearMonth.atDay(1);
        LocalDate fimMes = yearMonth.atEndOfMonth();

        List<LucroPorOsDto> detalhamento = contaRepository.findLucroPorOsNoPeriodo(ano, mes);

        long quantidade = osRepository.countByDataFinalizacaoBetween(inicioMes.atStartOfDay(), fimMes.atTime(23, 59, 59));

        Double receitaTotal = detalhamento.stream()
                .mapToDouble(LucroPorOsDto::getTotalReceita)
                .sum();

        Double custoTotal = detalhamento.stream()
                .mapToDouble(LucroPorOsDto::getTotalCustos)
                .sum();

        Double lucroTotal = receitaTotal - custoTotal;

        return RelatorioOsMensalDto.builder()
                .ano(ano)
                .mes(mes)
                .quantidadeOsFinalizadas(quantidade)
                .receitaTotalOs(receitaTotal)
                .custoTotalOs(custoTotal)
                .lucroTotalOs(lucroTotal)
                .detalhamento(detalhamento)
                .build();
    }
}