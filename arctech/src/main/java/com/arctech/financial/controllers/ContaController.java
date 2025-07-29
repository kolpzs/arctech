package com.arctech.financial.controllers;

import com.arctech.financial.dto.ContaDto;
import com.arctech.financial.dto.ContaResponseDto;
import com.arctech.financial.entities.Conta;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.services.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/find")
    public ResponseEntity<ContaResponseDto> findById(@RequestParam Long id) {
        return contaService.findById(id)
                .map(conta -> ResponseEntity.ok(new ContaResponseDto(conta)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<ContaResponseDto> update(@RequestParam Long id, @Valid @RequestBody ContaDto contaDto) {
        Conta contaAtualizada = contaService.update(id, contaDto);
        return ResponseEntity.ok(new ContaResponseDto(contaAtualizada));
    }

    // ALTERADO: de @PathVariable para @RequestParam
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
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

    @PatchMapping("/pagar")
    public ResponseEntity<ContaResponseDto> realizarPagamento(@RequestParam Long id) {
        Conta contaPaga = contaService.realizarPagamento(id);
        return ResponseEntity.ok(new ContaResponseDto(contaPaga));
    }

    @GetMapping("/despesas")
    public ResponseEntity<List<ContaResponseDto>> getContasAPagar(
            @RequestParam(required = false) Optional<StatusConta> status) {

        List<Conta> despesas = contaService.findDespesas(status);
        List<ContaResponseDto> dtos = despesas.stream()
                .map(ContaResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}