package com.arctech.financial.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.financial.entities.Categoria;
import com.arctech.financial.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void deleteById(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria com ID " + id + " não encontrada para exclusão.");
        }
        categoriaRepository.deleteById(id);
    }
}