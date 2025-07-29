package com.arctech.reports.services;

import com.arctech.financial.entities.Conta;
import com.arctech.financial.services.ContaService;
import com.arctech.reports.dtos.ExtratoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExtratoService {

    @Autowired
    private ContaService contaService;

    public ExtratoDto gerarExtrato(LocalDate dataInicio, LocalDate dataFim) {
        List<Conta> transacoes = contaService.findContasByPeriodo(dataInicio, dataFim);
        Float receitas = contaService.getTotalReceitasPorPeriodo(dataInicio, dataFim);
        Float despesas = contaService.getTotalDespesasPorPeriodo(dataInicio, dataFim);
        Float saldo = receitas - despesas;
        return new ExtratoDto(dataInicio, dataFim, receitas, despesas, saldo, transacoes);
    }
}