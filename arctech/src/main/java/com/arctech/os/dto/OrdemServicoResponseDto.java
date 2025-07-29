package com.arctech.os.dto;

import com.arctech.os.entities.OrdemServico;
import com.arctech.os.enums.StatusOS;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrdemServicoResponseDto {

    private Long id;
    private String tecnicoNome;
    private String clienteNome;
    private String servicoPrestadoNome;
    private Float servicoPrestadoValor;
    private StatusOS status;
    private String equipamento;
    private String problemaConstatado;
    private String problemaIdentificado;
    private String acoesTomadas;
    private Float custos;
    private Float valorTotal;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataRecebimentoEquipamento;
    private LocalDate dataEstipulada;
    private LocalDateTime dataFinalizacao;

    public OrdemServicoResponseDto(OrdemServico os) {
        this.id = os.getId();
        if (os.getTecnico() != null) {
            this.tecnicoNome = os.getTecnico().getName();
        }
        this.clienteNome = os.getCliente().getNome();
        this.servicoPrestadoNome = os.getServicoPrestado().getNome();
        this.servicoPrestadoValor = os.getServicoPrestado().getValor();
        this.status = os.getStatus();
        this.equipamento = os.getEquipamento();
        this.problemaConstatado = os.getProblemaConstatado();
        this.problemaIdentificado = os.getProblemaIdentificado();
        this.acoesTomadas = os.getAcoesTomadas();
        this.custos = os.getCustos() != null ? os.getCustos() : 0.0f;
        this.valorTotal = (os.getServicoPrestado().getValor() != null ? os.getServicoPrestado().getValor() : 0.0f) + this.custos;
        this.dataAbertura = os.getDataAbertura();
        this.dataRecebimentoEquipamento = os.getDataRecebimentoEquipamento();
        this.dataEstipulada = os.getDataEstipulada();
        this.dataFinalizacao = os.getDataFinalizacao();
    }
}