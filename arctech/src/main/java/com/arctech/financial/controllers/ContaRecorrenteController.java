package com.arctech.financial.controllers;

import com.arctech.financial.dto.ContaRecorrenteDto;
import com.arctech.financial.services.ContaRecorrenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recorrencias")
public class ContaRecorrenteController {

    @Autowired
    private ContaRecorrenteService recorrenciaService;

    @GetMapping
    public ResponseEntity<List<ContaRecorrenteDto>> findByUser() {
        return ResponseEntity.ok(recorrenciaService.findByUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaRecorrenteDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(recorrenciaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ContaRecorrenteDto> create(@Valid @RequestBody ContaRecorrenteDto dto) {
        ContaRecorrenteDto newDto = recorrenciaService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaRecorrenteDto> update(@PathVariable Long id, @Valid @RequestBody ContaRecorrenteDto dto) {
        ContaRecorrenteDto updatedDto = recorrenciaService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recorrenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}