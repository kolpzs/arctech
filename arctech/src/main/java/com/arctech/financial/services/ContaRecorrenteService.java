package com.arctech.financial.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.financial.dto.ContaRecorrenteDto;
import com.arctech.financial.entities.Categoria;
import com.arctech.financial.entities.Conta;
import com.arctech.financial.entities.ContaRecorrente;
import com.arctech.financial.entities.Instituicao;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.repositories.CategoriaRepository;
import com.arctech.financial.repositories.ContaRecorrenteRepository;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.repositories.InstituicaoRepository;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaRecorrenteService {

    @Autowired
    private ContaRecorrenteRepository recorrenteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    private static Conta getConta(ContaRecorrente recorrencia, LocalDate dataVencimentoEsteMes) {
        Conta novaConta = new Conta();
        novaConta.setDescricao(recorrencia.getDescricao());
        novaConta.setValor(recorrencia.getValor());
        novaConta.setTipo(recorrencia.getTipo());
        novaConta.setCategoria(recorrencia.getCategoria());
        novaConta.setInstituicao(recorrencia.getInstituicao());
        novaConta.setUser(recorrencia.getUser());
        novaConta.setDataVencimento(dataVencimentoEsteMes);
        novaConta.setStatus(StatusConta.PENDENTE);
        novaConta.setContaRecorrente(recorrencia);
        return novaConta;
    }

    private User getAuthenticatedUser() {
        Jwt jwt = contaService.getAuthenticatedUserAsJwt();
        return userService.findOrCreateUserFromJwt(jwt);
    }

    @Transactional(readOnly = true)
    public List<ContaRecorrenteDto> findByUser() {
        User user = getAuthenticatedUser();
        List<ContaRecorrente> recorrencias = recorrenteRepository.findByUser(user);
        return recorrencias.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaRecorrenteDto findById(Long id) {
        User user = getAuthenticatedUser();
        ContaRecorrente recorrencia = recorrenteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recorrência não encontrada com ID: " + id));
        return mapEntityToDto(recorrencia);
    }

    @Transactional
    public ContaRecorrenteDto create(ContaRecorrenteDto dto) {
        User user = getAuthenticatedUser();
        ContaRecorrente entity = new ContaRecorrente();
        mapDtoToEntity(dto, entity);
        entity.setUser(user);
        entity = recorrenteRepository.save(entity);
        return mapEntityToDto(entity);
    }

    @Transactional
    public ContaRecorrenteDto update(Long id, ContaRecorrenteDto dto) {
        User user = getAuthenticatedUser();
        ContaRecorrente entity = recorrenteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recorrência não encontrada para atualização com ID: " + id));
        mapDtoToEntity(dto, entity);
        entity = recorrenteRepository.save(entity);
        return mapEntityToDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        User user = getAuthenticatedUser();
        if (!recorrenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recorrência não encontrada para exclusão com ID: " + id);
        }
        recorrenteRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Você não tem permissão para deletar esta recorrência."));
        recorrenteRepository.deleteById(id);
    }

    private void mapDtoToEntity(ContaRecorrenteDto dto, ContaRecorrente entity) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + dto.getCategoriaId()));
        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada com ID: " + dto.getInstituicaoId()));

        entity.setDescricao(dto.getDescricao());
        entity.setValor(dto.getValor());
        entity.setTipo(dto.getTipo());
        entity.setFrequencia(dto.getFrequencia());
        entity.setDiaDoVencimento(dto.getDiaDoVencimento());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setCategoria(categoria);
        entity.setInstituicao(instituicao);
    }

    private ContaRecorrenteDto mapEntityToDto(ContaRecorrente entity) {
        ContaRecorrenteDto dto = new ContaRecorrenteDto();
        dto.setId(entity.getId());
        dto.setDescricao(entity.getDescricao());
        dto.setValor(entity.getValor());
        dto.setTipo(entity.getTipo());
        dto.setFrequencia(entity.getFrequencia());
        dto.setDiaDoVencimento(entity.getDiaDoVencimento());
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataFim(entity.getDataFim());
        dto.setCategoriaId(entity.getCategoria().getId());
        dto.setInstituicaoId(entity.getInstituicao().getId());
        return dto;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * ?") // Executa todo dia 1, à 01:00 da manhã
    public void gerarContasDoMes() {
        System.out.println("Iniciando job de geração de contas recorrentes...");
        LocalDate hoje = LocalDate.now();

        List<ContaRecorrente> recorrenciasAtivas = recorrenteRepository.findRecorrenciasAtivasEm(hoje);
        System.out.println(recorrenciasAtivas.size() + " recorrências ativas encontradas para o mês atual.");

        for (ContaRecorrente recorrencia : recorrenciasAtivas) {
            LocalDate dataVencimentoEsteMes = hoje.withDayOfMonth(recorrencia.getDiaDoVencimento());

            boolean jaExiste = contaRepository.existsByContaRecorrenteAndDataVencimento(recorrencia, dataVencimentoEsteMes);

            if (!jaExiste) {
                System.out.println("Gerando conta para a recorrência: " + recorrencia.getDescricao());
                Conta novaConta = getConta(recorrencia, dataVencimentoEsteMes);
                contaRepository.save(novaConta);
            } else {
                System.out.println("Conta para a recorrência '" + recorrencia.getDescricao() + "' já existe para o vencimento " + dataVencimentoEsteMes);
            }
        }
        System.out.println("Job de geração de contas recorrentes finalizado.");
    }
}