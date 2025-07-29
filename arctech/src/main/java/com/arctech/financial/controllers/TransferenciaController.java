package com.arctech.financial.controllers;

import com.arctech.financial.dto.TransferenciaDto;
import com.arctech.financial.services.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<Void> realizarTransferencia(@Valid @RequestBody TransferenciaDto dto) {
        transferenciaService.realizarTransferencia(dto);
        return ResponseEntity.ok().build();
    }
}