package com.arctech.controllers;

import com.arctech.entities.Instituicao;
import com.arctech.services.InstituicaoService;
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

    @GetMapping("/{id}")
    public ResponseEntity<Instituicao> findById(@PathVariable Long id) {
        return instituicaoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Instituicao> create(@RequestBody Instituicao instituicao) {
        Instituicao novaInstituicao = instituicaoService.save(instituicao);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaInstituicao.getId()).toUri();
        return ResponseEntity.created(uri).body(novaInstituicao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instituicao> update(@PathVariable Long id, @RequestBody Instituicao instituicao) {
        instituicao.setId(id);
        Instituicao instituicaoAtualizada = instituicaoService.save(instituicao);
        return ResponseEntity.ok(instituicaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            instituicaoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}