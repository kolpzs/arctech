package com.arctech.reports.controllers;

import com.arctech.reports.dto.RelatorioOsMensalDto;
import com.arctech.reports.services.RelatorioOsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorios/os")
public class RelatorioOsController {

    @Autowired
    private RelatorioOsService relatorioOsService;

    @GetMapping("/mensal")
    public ResponseEntity<RelatorioOsMensalDto> getRelatorioMensal(
            @RequestParam int ano,
            @RequestParam int mes) {

        RelatorioOsMensalDto relatorio = relatorioOsService.gerarRelatorioMensal(ano, mes);
        return ResponseEntity.ok(relatorio);
    }
}