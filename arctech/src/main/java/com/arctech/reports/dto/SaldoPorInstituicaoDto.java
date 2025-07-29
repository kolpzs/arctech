package com.arctech.reports.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaldoPorInstituicaoDto {
    private String nomeInstituicao;
    private Double saldo;
}