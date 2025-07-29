package com.arctech.os.entities;

import com.arctech.os.enums.OrigemCliente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column
    private String email;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String documentoEstrangeiro;

    @Column(nullable = false)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigemCliente origem;
}