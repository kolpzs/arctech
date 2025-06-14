package com.arctech.services;

import com.arctech.dto.ExtratoDto;
import com.arctech.entities.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExtratoService {

    @Autowired
    private ContaService contaService;

    public ExtratoDto gerarExtrato(Date dataInicio, Date dataFim) {

        List<Conta> transacoes = contaService.findContasByPeriodo(dataInicio, dataFim);

        Float receitas = contaService.getTotalReceitasPorPeriodo(dataInicio, dataFim);
        Float despesas = contaService.getTotalDespesasPorPeriodo(dataInicio, dataFim);

        Float saldo = receitas - despesas;

        return new ExtratoDto(dataInicio, dataFim, receitas, despesas, saldo, transacoes);
    }
}