package com.arctech.reports.controllers;

import com.arctech.reports.dto.DashboardDto;
import com.arctech.reports.dto.SaldoPorInstituicaoDto;
import com.arctech.reports.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    @GetMapping("/saldo-por-instituicao")
    public ResponseEntity<List<SaldoPorInstituicaoDto>> getSaldoPorInstituicao() {
        return ResponseEntity.ok(dashboardService.getSaldoPorInstituicao());
    }
}