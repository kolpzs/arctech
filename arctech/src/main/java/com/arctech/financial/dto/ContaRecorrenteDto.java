package com.arctech.financial.dto;

import com.arctech.financial.entities.ContaRecorrente.Frequencia;
import com.arctech.financial.enums.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ContaRecorrenteDto {

    private Long id;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    private Float valor;

    @NotNull(message = "O tipo (RECEITA, DESPESA) é obrigatório.")
    private Tipo tipo;

    @NotNull(message = "A frequência é obrigatória.")
    private Frequencia frequencia;

    @NotNull(message = "O dia do vencimento é obrigatório.")
    private int diaDoVencimento;

    @NotNull(message = "A data de início é obrigatória.")
    private LocalDate dataInicio;

    private LocalDate dataFim; // Opcional

    @NotNull(message = "O ID da categoria é obrigatório.")
    private Long categoriaId;

    @NotNull(message = "O ID da instituição é obrigatório.")
    private Long instituicaoId;
}