package com.arctech.financial.controllers;

import com.arctech.financial.entities.Instituicao;
import com.arctech.financial.services.InstituicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    @Autowired
    private InstituicaoService instituicaoService;

    @GetMapping
    public ResponseEntity<List<Instituicao>> findAll() {
        return ResponseEntity.ok(instituicaoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Instituicao> create(@RequestBody Instituicao instituicao) {
        Instituicao novaInstituicao = instituicaoService.save(instituicao);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaInstituicao.getId()).toUri();
        return ResponseEntity.created(uri).body(novaInstituicao);
    }

    @GetMapping("/find")
    public ResponseEntity<Instituicao> findById(@RequestParam Long id) {
        return instituicaoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Instituicao> update(@RequestParam Long id, @RequestBody Instituicao instituicao) {
        instituicao.setId(id);
        Instituicao instituicaoAtualizada = instituicaoService.save(instituicao);
        return ResponseEntity.ok(instituicaoAtualizada);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        try {
            instituicaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}