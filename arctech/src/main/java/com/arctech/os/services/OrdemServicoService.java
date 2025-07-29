package com.arctech.os.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.os.dto.OrdemServicoRequestDto;
import com.arctech.os.dto.OrdemServicoResponseDto;
import com.arctech.os.entities.Cliente;
import com.arctech.os.entities.OrdemServico;
import com.arctech.os.entities.Servico;
import com.arctech.os.enums.StatusOS;
import com.arctech.os.repositories.ClienteRepository;
import com.arctech.os.repositories.OrdemServicoRepository;
import com.arctech.os.repositories.ServicoRepository;
import com.arctech.users.entities.User;
import com.arctech.users.repositories.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdemServicoService {

    @Autowired
    private OrdemServicoRepository osRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public OrdemServicoResponseDto findById(Long id) {
        OrdemServico os = osRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com ID: " + id));
        return new OrdemServicoResponseDto(os);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDto> findAll() {
        List<OrdemServico> list = osRepository.findAll();
        return list.stream().map(OrdemServicoResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public OrdemServicoResponseDto create(OrdemServicoRequestDto dto) {
        User tecnico = userRepository.findById(dto.getTecnicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado com ID: " + dto.getTecnicoId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));
        Servico servico = servicoRepository.findById(dto.getServicoPrestadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + dto.getServicoPrestadoId()));

        OrdemServico entity = new OrdemServico();
        mapDtoToEntity(dto, entity, tecnico, cliente, servico);
        entity = osRepository.save(entity);

        return new OrdemServicoResponseDto(entity);
    }

    @Transactional
    public OrdemServicoResponseDto update(Long id, OrdemServicoRequestDto dto) {
        OrdemServico entity = osRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada para atualização com ID: " + id));

        User tecnico = userRepository.findById(dto.getTecnicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado com ID: " + dto.getTecnicoId()));
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));
        Servico servico = servicoRepository.findById(dto.getServicoPrestadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com ID: " + dto.getServicoPrestadoId()));

        mapDtoToEntity(dto, entity, tecnico, cliente, servico);

        if (dto.getStatus() == StatusOS.FINALIZADA) {
            entity.setDataFinalizacao(LocalDateTime.now());
        }

        entity = osRepository.save(entity);
        return new OrdemServicoResponseDto(entity);
    }

    @Transactional
    public OrdemServicoResponseDto cancel(Long id) {
        OrdemServico entity = osRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada para cancelamento com ID: " + id));

        if (entity.getStatus() == StatusOS.FINALIZADA || entity.getStatus() == StatusOS.CANCELADA) {
            throw new ValidationException("Não é possível cancelar uma Ordem de Serviço que já foi finalizada ou cancelada.");
        }

        entity.setStatus(StatusOS.CANCELADA);
        entity.setDataFinalizacao(LocalDateTime.now());
        entity = osRepository.save(entity);
        return new OrdemServicoResponseDto(entity);
    }

    private void mapDtoToEntity(OrdemServicoRequestDto dto, OrdemServico entity, User tecnico, Cliente cliente, Servico servico) {
        entity.setTecnico(tecnico);
        entity.setCliente(cliente);
        entity.setServicoPrestado(servico);
        entity.setStatus(dto.getStatus());
        entity.setEquipamento(dto.getEquipamento());
        entity.setProblemaConstatado(dto.getProblemaConstatado());
        entity.setProblemaIdentificado(dto.getProblemaIdentificado());
        entity.setAcoesTomadas(dto.getAcoesTomadas());
        entity.setCustos(dto.getCustos());
        entity.setDataRecebimentoEquipamento(dto.getDataRecebimentoEquipamento());
        entity.setDataEstipulada(dto.getDataEstipulada());
    }

    @Transactional
    public OrdemServicoResponseDto updateStatus(Long id, StatusOS newStatus) {
        OrdemServico entity = osRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com ID: " + id));

        if (entity.getStatus() == StatusOS.FINALIZADA || entity.getStatus() == StatusOS.CANCELADA) {
            throw new ValidationException("O status de uma OS finalizada ou cancelada não pode ser alterado.");
        }

        entity.setStatus(newStatus);

        if (newStatus == StatusOS.FINALIZADA || newStatus == StatusOS.CANCELADA) {
            entity.setDataFinalizacao(LocalDateTime.now());
        }

        entity = osRepository.save(entity);
        return new OrdemServicoResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDto> findByStatus(StatusOS status) {
        List<OrdemServico> list = osRepository.findByStatusOrderByDataAberturaDesc(status);
        return list.stream().map(OrdemServicoResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDto> findByTecnico(Long tecnicoId) {
        User tecnico = userRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado com ID: " + tecnicoId));
        List<OrdemServico> list = osRepository.findByTecnico(tecnico);
        return list.stream().map(OrdemServicoResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDto> findAtrasadas() {
        List<StatusOS> statusAbertos = Arrays.asList(
                StatusOS.ABERTA,
                StatusOS.EM_ANDAMENTO,
                StatusOS.AGUARDANDO_PECA,
                StatusOS.AGUARDANDO_APROVACAO_CLIENTE
        );
        List<OrdemServico> list = osRepository.findAtrasadas(LocalDate.now(), statusAbertos);
        return list.stream().map(OrdemServicoResponseDto::new).collect(Collectors.toList());
    }
}