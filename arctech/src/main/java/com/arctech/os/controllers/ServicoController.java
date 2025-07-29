package com.arctech.os.controllers;

import com.arctech.os.dto.ServicoDto;
import com.arctech.os.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping("/find")
    public ResponseEntity<ServicoDto> findById(@RequestParam Long id) {
        ServicoDto dto = servicoService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ServicoDto>> findAll() {
        List<ServicoDto> list = servicoService.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ServicoDto> create(@Valid @RequestBody ServicoDto dto) {
        ServicoDto newDto = servicoService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/find").queryParam("id", newDto.getId()).build().toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping
    public ResponseEntity<ServicoDto> update(@RequestParam Long id, @Valid @RequestBody ServicoDto dto) { // Anotação alterada
        ServicoDto updatedDto = servicoService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deactivate(@RequestParam Long id) { // Anotação alterada
        servicoService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reactivate")
    public ResponseEntity<Void> reactivate(@RequestParam Long id) { // Anotação alterada
        servicoService.reactivate(id);
        return ResponseEntity.ok().build();
    }
}