package com.arctech.dto;

import com.arctech.entities.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExtratoDto {

    private Date dataInicial;
    private Date dataFinal;
    private Float totalReceitasPeriodo;
    private Float totalDespesasPeriodo;
    private Float saldoPeriodo;
    private List<Conta> transacoes;

}