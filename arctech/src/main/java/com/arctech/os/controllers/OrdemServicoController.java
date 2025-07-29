package com.arctech.os.controllers;

import com.arctech.os.dto.OrdemServicoRequestDto;
import com.arctech.os.dto.OrdemServicoResponseDto;
import com.arctech.os.enums.StatusOS;
import com.arctech.os.services.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService osService;

    @GetMapping("/find")
    public ResponseEntity<OrdemServicoResponseDto> findById(@RequestParam Long id) {
        return ResponseEntity.ok(osService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDto>> findAll() {
        return ResponseEntity.ok(osService.findAll());
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDto> create(@Valid @RequestBody OrdemServicoRequestDto dto) {
        OrdemServicoResponseDto newDto = osService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/find").queryParam("id", newDto.getId()).build().toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping
    public ResponseEntity<OrdemServicoResponseDto> update(@RequestParam Long id, @Valid @RequestBody OrdemServicoRequestDto dto) { // Anotação alterada
        return ResponseEntity.ok(osService.update(id, dto));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<OrdemServicoResponseDto> cancel(@RequestParam Long id) { // Anotação alterada
        return ResponseEntity.ok(osService.cancel(id));
    }

    @PatchMapping("/status")
    public ResponseEntity<OrdemServicoResponseDto> updateStatus(@RequestParam Long id, @RequestBody StatusOS newStatus) { // Anotação alterada
        return ResponseEntity.ok(osService.updateStatus(id, newStatus));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<OrdemServicoResponseDto>> getByStatus(@RequestParam StatusOS status) {
        return ResponseEntity.ok(osService.findByStatus(status));
    }

    @GetMapping("/by-tecnico")
    public ResponseEntity<List<OrdemServicoResponseDto>> getByTecnico(@RequestParam Long tecnicoId) {
        return ResponseEntity.ok(osService.findByTecnico(tecnicoId));
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<List<OrdemServicoResponseDto>> getAtrasadas() {
        return ResponseEntity.ok(osService.findAtrasadas());
    }
}