package com.arctech.os.dto;

import com.arctech.os.enums.StatusOS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrdemServicoRequestDto {

    @NotNull(message = "O ID do técnico é obrigatório.")
    private Long tecnicoId;

    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "O ID do serviço prestado é obrigatório.")
    private Long servicoPrestadoId;

    @NotNull(message = "O status é obrigatório.")
    private StatusOS status;

    @NotBlank(message = "A descrição do equipamento é obrigatória.")
    private String equipamento;

    @NotBlank(message = "O problema constatado é obrigatório.")
    private String problemaConstatado;

    private String problemaIdentificado;

    private String acoesTomadas;

    private Float custos;

    private LocalDateTime dataRecebimentoEquipamento;

    private LocalDate dataEstipulada;
}