package com.arctech.controllers;

import com.arctech.dto.ExtratoDto;
import com.arctech.services.ExtratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/extratos")
public class ExtratoController {

    @Autowired
    private ExtratoService extratoService;

    @GetMapping
    public ResponseEntity<ExtratoDto> getExtrato(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataInicio,
            @RequestParam("dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dataFim) {

        ExtratoDto extratoDto = extratoService.gerarExtrato(dataInicio, dataFim);
        return ResponseEntity.ok(extratoDto);
    }
}