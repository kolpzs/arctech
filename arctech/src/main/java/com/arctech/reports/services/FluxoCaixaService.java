package com.arctech.reports.services;

import com.arctech.financial.entities.Conta;
import com.arctech.financial.enums.Tipo;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.services.ContaService;
import com.arctech.reports.dtos.FluxoCaixaDto;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FluxoCaixaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    // Injeção do UserService, que faltava
    @Autowired
    private UserService userService;

    public FluxoCaixaDto gerarFluxoCaixa(LocalDate dataInicio, LocalDate dataFim) {
        // Correção: Obter primeiro o JWT e depois o User
        Jwt jwt = contaService.getAuthenticatedUserAsJwt();
        User user = userService.findOrCreateUserFromJwt(jwt);

        // 1. Pega todas as transações no período
        List<Conta> transacoes = contaRepository.findByUserAndDataVencimentoBetweenOrderByDataVencimentoAsc(user, dataInicio, dataFim);

        // 2. Calcula totais do período
        Float totalReceitas = transacoes.stream()
                .filter(c -> c.getTipo() == Tipo.RECEITA)
                .map(Conta::getValor)
                .reduce(0.0f, Float::sum);

        Float totalDespesas = transacoes.stream()
                .filter(c -> c.getTipo() == Tipo.DESPESA)
                .map(Conta::getValor)
                .reduce(0.0f, Float::sum);

        // 3. Calcula saldos inicial e final
        Float saldoInicial = contaRepository.findSaldoAteData(user, dataInicio);
        Float saldoFinal = saldoInicial + totalReceitas - totalDespesas;

        // 4. Agrupa transações por dia
        Map<LocalDate, List<Conta>> transacoesPorDia = transacoes.stream()
                .collect(Collectors.groupingBy(Conta::getDataVencimento));

        List<FluxoCaixaDto.FluxoCaixaDiario> fluxoDiarioList = new ArrayList<>();
        transacoesPorDia.forEach((data, contasDoDia) -> {
            Float receitasDia = contasDoDia.stream().filter(c -> c.getTipo() == Tipo.RECEITA).map(Conta::getValor).reduce(0.0f, Float::sum);
            Float despesasDia = contasDoDia.stream().filter(c -> c.getTipo() == Tipo.DESPESA).map(Conta::getValor).reduce(0.0f, Float::sum);

            fluxoDiarioList.add(FluxoCaixaDto.FluxoCaixaDiario.builder()
                    .data(data)
                    .totalReceitas(receitasDia)
                    .totalDespesas(despesasDia)
                    .saldoDoDia(receitasDia - despesasDia)
                    .build());
        });

        // Correção: Ordenar a lista antes de construir o DTO final
        fluxoDiarioList.sort(Comparator.comparing(FluxoCaixaDto.FluxoCaixaDiario::getData));

        return FluxoCaixaDto.builder()
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .saldoInicialPeriodo(saldoInicial)
                .saldoFinalPeriodo(saldoFinal)
                .totalReceitasPeriodo(totalReceitas)
                .totalDespesasPeriodo(totalDespesas)
                .fluxoDiario(fluxoDiarioList)
                .build();
    }
}