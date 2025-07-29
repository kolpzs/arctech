package com.arctech.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class LucroPorOsDto {
    private Long osId;
    private String clienteNome;
    private LocalDateTime dataFinalizacao;
    private Double totalReceita;
    private Double totalCustos;
    private Double lucro;
}