package com.arctech.financial.controllers;

import com.arctech.financial.entities.Categoria;
import com.arctech.financial.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<Categoria> findById(@RequestParam Long id) {
        return categoriaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.save(categoria);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaCategoria.getId()).toUri();
        return ResponseEntity.created(uri).body(novaCategoria);
    }

    @PutMapping("/update")
    public ResponseEntity<Categoria> update(@RequestParam Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        Categoria categoriaAtualizada = categoriaService.save(categoria);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}