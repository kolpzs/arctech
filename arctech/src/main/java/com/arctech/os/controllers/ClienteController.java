package com.arctech.os.controllers;

import com.arctech.os.dto.ClienteDto;
import com.arctech.os.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/find")
    public ResponseEntity<ClienteDto> findById(@RequestParam Long id) {
        ClienteDto dto = clienteService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDto>> findAll() {
        List<ClienteDto> list = clienteService.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ClienteDto> create(@Valid @RequestBody ClienteDto dto) {
        ClienteDto newDto = clienteService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/find").queryParam("id", newDto.getId()).build().toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping
    public ResponseEntity<ClienteDto> update(@RequestParam Long id, @Valid @RequestBody ClienteDto dto) { // Anotação alterada
        ClienteDto updatedDto = clienteService.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
}