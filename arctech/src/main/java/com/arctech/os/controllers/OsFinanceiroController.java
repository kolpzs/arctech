package com.arctech.os.controllers;

import com.arctech.os.dto.FinalizarOsRequestDto;
import com.arctech.os.services.OsFinanceiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/os-financeiro")
public class OsFinanceiroController {

    @Autowired
    private OsFinanceiroService osFinanceiroService;

    @PostMapping("/lancar")
    public ResponseEntity<Void> lancarFinanceiro(
            @RequestParam Long osId,
            @Valid @RequestBody FinalizarOsRequestDto dto) {

        osFinanceiroService.lancarFinanceiro(osId, dto);
        return ResponseEntity.ok().build();
    }
}