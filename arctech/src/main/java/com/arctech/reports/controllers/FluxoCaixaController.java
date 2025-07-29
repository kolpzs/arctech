package com.arctech.reports.controllers;

import com.arctech.reports.dto.FluxoCaixaDto;
import com.arctech.reports.services.FluxoCaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/relatorios/fluxo-caixa")
public class FluxoCaixaController {

    @Autowired
    private FluxoCaixaService fluxoCaixaService;

    @GetMapping
    public ResponseEntity<FluxoCaixaDto> getFluxoCaixa(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return ResponseEntity.ok(fluxoCaixaService.gerarFluxoCaixa(dataInicio, dataFim));
    }
}