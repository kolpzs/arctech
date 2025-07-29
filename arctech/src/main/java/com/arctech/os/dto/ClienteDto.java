package com.arctech.os.dto;

import com.arctech.os.enums.OrigemCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDto {

    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    private String cpf;

    private String documentoEstrangeiro;

    @NotBlank(message = "A cidade é obrigatória.")
    private String cidade;

    @NotNull(message = "A origem do cliente é obrigatória.")
    private OrigemCliente origem;
}