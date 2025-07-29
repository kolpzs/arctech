package com.arctech.os.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FinalizarOsRequestDto {

    @NotNull(message = "A data de recebimento é obrigatória.")
    private LocalDate dataRecebimento;

    @NotNull(message = "O ID da instituição de destino do valor é obrigatório.")
    private Long instituicaoReceitaId;
}