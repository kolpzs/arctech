package com.arctech.controllers;

import com.arctech.dto.ContaDto;
import com.arctech.dto.ContaResponseDto;
import com.arctech.entities.Conta;
import com.arctech.services.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public ResponseEntity<ContaResponseDto> create(@Valid @RequestBody ContaDto contaDto) {
        Conta novaConta = contaService.save(contaDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaConta.getId()).toUri();
        return ResponseEntity.created(uri).body(new ContaResponseDto(novaConta));
    }

    @GetMapping
    public ResponseEntity<List<ContaResponseDto>> findAll() {
        List<Conta> contas = contaService.findAll();
        List<ContaResponseDto> dtos = contas.stream()
                .map(ContaResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDto> findById(@PathVariable Long id) {
        return contaService.findById(id)
                .map(conta -> ResponseEntity.ok(new ContaResponseDto(conta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaResponseDto> update(@PathVariable Long id, @Valid @RequestBody ContaDto contaDto) {
        Conta contaAtualizada = contaService.update(id, contaDto);
        return ResponseEntity.ok(new ContaResponseDto(contaAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            contaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/saldo")
    public ResponseEntity<Float> getSaldo() {
        return ResponseEntity.ok(contaService.getSaldoAtual());
    }
}