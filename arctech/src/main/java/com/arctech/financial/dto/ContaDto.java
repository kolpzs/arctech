package com.arctech.financial.dto;

import com.arctech.financial.enums.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ContaDto {

    @NotNull(message = "A data não pode ser nula.")
    private LocalDate dataVencimento;

    @NotBlank(message = "A descrição não pode ser vazia.")
    private String descricao;

    @NotNull(message = "O valor não pode ser nulo.")
    @Positive(message = "O valor deve ser positivo.")
    private Float valor;

    @NotNull(message = "O tipo (RECEITA, DESPESA) não pode ser nulo.")
    private Tipo tipo;

    private Long categoriaId;

    @NotNull(message = "O ID da instituição não pode ser nulo.")
    private Long instituicaoId;
}