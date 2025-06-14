package com.arctech.services;

import com.arctech.entities.Instituicao;
import com.arctech.repositories.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    public List<Instituicao> findAll() {
        return instituicaoRepository.findAll();
    }

    public Optional<Instituicao> findById(Long id) {
        return instituicaoRepository.findById(id);
    }

    public Instituicao save(Instituicao instituicao) {
        return instituicaoRepository.save(instituicao);
    }

    public void deleteById(Long id) {
        if (!instituicaoRepository.existsById(id)) {
            throw new RuntimeException("Instituição com ID " + id + " não encontrada para exclusão.");
        }
        instituicaoRepository.deleteById(id);
    }
}