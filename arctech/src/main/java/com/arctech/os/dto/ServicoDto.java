package com.arctech.os.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServicoDto {

    private Long id;

    @NotBlank(message = "O nome do serviço é obrigatório.")
    private String nome;

    @NotNull(message = "O valor do serviço é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    private Float valor;
}