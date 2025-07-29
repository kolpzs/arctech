package com.arctech.financial.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransferenciaDto {

    @NotNull(message = "O ID da instituição de origem não pode ser nulo.")
    private Long idInstituicaoOrigem;

    @NotNull(message = "O ID da instituição de destino não pode ser nulo.")
    private Long idInstituicaoDestino;

    @NotNull(message = "O valor não pode ser nulo.")
    @Positive(message = "O valor da transferência deve ser positivo.")
    private Float valor;

    @NotNull(message = "A data da transferência não pode ser nula.")
    private LocalDate data;

    private String descricao;
}