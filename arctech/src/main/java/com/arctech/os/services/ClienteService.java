package com.arctech.os.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.os.dto.ClienteDto;
import com.arctech.os.entities.Cliente;
import com.arctech.os.repositories.ClienteRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public ClienteDto findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        return mapEntityToDto(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteDto> findAll() {
        List<Cliente> list = clienteRepository.findAll();
        return list.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public ClienteDto create(ClienteDto dto) {
        // Lógica para evitar duplicatas
        if (clienteRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new ValidationException("CPF já cadastrado no sistema.");
        }
        if (clienteRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("E-mail já cadastrado no sistema.");
        }
        if (clienteRepository.findByTelefone(dto.getTelefone()).isPresent()) {
            throw new ValidationException("Telefone já cadastrado no sistema.");
        }

        Cliente entity = new Cliente();
        mapDtoToEntity(dto, entity);
        entity = clienteRepository.save(entity);
        return mapEntityToDto(entity);
    }

    @Transactional
    public ClienteDto update(Long id, ClienteDto dto) {
        Cliente entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para atualização com ID: " + id));
        mapDtoToEntity(dto, entity);
        entity = clienteRepository.save(entity);
        return mapEntityToDto(entity);
    }

    private ClienteDto mapEntityToDto(Cliente entity) {
        ClienteDto dto = new ClienteDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setCpf(entity.getCpf());
        dto.setDocumentoEstrangeiro(entity.getDocumentoEstrangeiro());
        dto.setCidade(entity.getCidade());
        dto.setOrigem(entity.getOrigem());
        return dto;
    }

    private void mapDtoToEntity(ClienteDto dto, Cliente entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());
        entity.setDocumentoEstrangeiro(dto.getDocumentoEstrangeiro());
        entity.setCidade(dto.getCidade());
        entity.setOrigem(dto.getOrigem());
    }
}