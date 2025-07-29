package com.arctech.reports.controllers;

import com.arctech.reports.dtos.ExtratoDto;
import com.arctech.reports.services.ExtratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/extratos")
public class ExtratoController {

    @Autowired
    private ExtratoService extratoService;

    @GetMapping
    public ResponseEntity<ExtratoDto> getExtrato(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio, // MUDANÇA AQUI
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) { // MUDANÇA AQUI

        ExtratoDto extratoDto = extratoService.gerarExtrato(dataInicio, dataFim);
        return ResponseEntity.ok(extratoDto);
    }
}