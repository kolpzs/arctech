package com.arctech.os.entities;

import com.arctech.os.enums.StatusOS;
import com.arctech.users.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens-servico")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private User tecnico;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "servico_prestado_id", nullable = false)
    private Servico servicoPrestado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOS status;

    @Column(nullable = false)
    private String equipamento;

    @Lob // Para textos mais longos
    @Column(nullable = false)
    private String problemaConstatado;

    @Lob
    private String problemaIdentificado;

    @Lob
    private String acoesTomadas;

    private Float custos; // Custos adicionais (peças, etc.)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    @Column
    private LocalDateTime dataRecebimentoEquipamento;

    @Column
    private LocalDate dataEstipulada;

    @Column
    private LocalDateTime dataFinalizacao;

    @Column(nullable = false)
    private boolean financeiroLancado = false;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = StatusOS.ABERTA;
        }
    }
}