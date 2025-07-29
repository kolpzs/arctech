package com.arctech.os.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.os.dto.ServicoDto;
import com.arctech.os.entities.Servico;
import com.arctech.os.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Transactional(readOnly = true)
    public ServicoDto findById(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + id));
        return mapEntityToDto(servico);
    }

    @Transactional(readOnly = true)
    public List<ServicoDto> findAll() {
        List<Servico> list = servicoRepository.findByAtivoTrue();
        return list.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public ServicoDto create(ServicoDto dto) {
        Servico entity = new Servico();
        mapDtoToEntity(dto, entity);
        entity.setAtivo(true);
        entity = servicoRepository.save(entity);
        return mapEntityToDto(entity);
    }

    @Transactional
    public ServicoDto update(Long id, ServicoDto dto) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado para atualização com ID: " + id));
        mapDtoToEntity(dto, entity);
        entity = servicoRepository.save(entity);
        return mapEntityToDto(entity);
    }

    @Transactional
    public void deactivate(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado para desativação com ID: " + id));
        entity.setAtivo(false);
        servicoRepository.save(entity);
    }

    @Transactional
    public void reactivate(Long id) {
        Servico entity = servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado para reativação com ID: " + id));
        entity.setAtivo(true);
        servicoRepository.save(entity);
    }

    private ServicoDto mapEntityToDto(Servico entity) {
        ServicoDto dto = new ServicoDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setValor(entity.getValor());
        return dto;
    }

    private void mapDtoToEntity(ServicoDto dto, Servico entity) {
        entity.setNome(dto.getNome());
        entity.setValor(dto.getValor());
    }
}