package com.arctech.financial.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.financial.dto.ContaDto;
import com.arctech.financial.entities.Categoria;
import com.arctech.financial.entities.Conta;
import com.arctech.financial.entities.Instituicao;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.enums.Tipo;
import com.arctech.financial.repositories.CategoriaRepository;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.repositories.InstituicaoRepository;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private UserService userService;

    public Jwt getAuthenticatedUserAsJwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private User getAuthenticatedUser() {
        Jwt jwt = getAuthenticatedUserAsJwt();
        return userService.findOrCreateUserFromJwt(jwt);
    }

    public Conta save(ContaDto contaDto) {
        User user = getAuthenticatedUser();
        Conta novaConta = new Conta();

        mapDtoToEntity(contaDto, novaConta);
        novaConta.setUser(user);

        novaConta.setStatus(StatusConta.PENDENTE);
        novaConta.setDataPagamento(null);

        return contaRepository.save(novaConta);
    }

    public List<Conta> findAll() {
        return contaRepository.findByUser(getAuthenticatedUser());
    }

    public Optional<Conta> findById(Long id) {
        return contaRepository.findByIdAndUser(id, getAuthenticatedUser());
    }

    public Conta update(Long id, ContaDto contaDto) {
        Conta contaExistente = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id + " não encontrada para este usuário."));

        mapDtoToEntity(contaDto, contaExistente);

        return contaRepository.save(contaExistente);
    }

    public void deleteById(Long id) {
        if (findById(id).isEmpty()) {
            throw new RuntimeException("Conta com ID " + id + " não encontrada para este usuário.");
        }
        contaRepository.deleteById(id);
    }

    // Este método agora aceita LocalDate
    public List<Conta> findContasByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return contaRepository.findByUserAndDataVencimentoBetweenOrderByDataVencimentoAsc(getAuthenticatedUser(), dataInicio, dataFim);
    }

    public Float getTotalReceitas() {
        return contaRepository.sumByTipoAndUser(Tipo.RECEITA, getAuthenticatedUser()).orElse(0.0f);
    }

    public Float getTotalDespesas() {
        return contaRepository.sumByTipoAndUser(Tipo.DESPESA, getAuthenticatedUser()).orElse(0.0f);
    }

    public Float getSaldoAtual() {
        return getTotalReceitas() - getTotalDespesas();
    }

    // Este método agora aceita LocalDate
    public Float getTotalReceitasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return contaRepository.sumByTipoAndDataVencimentoBetween(Tipo.RECEITA, getAuthenticatedUser(), dataInicio, dataFim).orElse(0.0f);
    }

    // Este método agora aceita LocalDate
    public Float getTotalDespesasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return contaRepository.sumByTipoAndDataVencimentoBetween(Tipo.DESPESA, getAuthenticatedUser(), dataInicio, dataFim).orElse(0.0f);
    }

    private void mapDtoToEntity(ContaDto dto, Conta conta) {
        Instituicao instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição não encontrada com ID: " + dto.getInstituicaoId()));
        Categoria categoria = null;
        if (dto.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + dto.getCategoriaId()));
        }
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setDescricao(dto.getDescricao());
        conta.setValor(dto.getValor());
        conta.setTipo(dto.getTipo());
        conta.setInstituicao(instituicao);
        conta.setCategoria(categoria);
    }

    @Transactional
    public Conta realizarPagamento(Long contaId) {
        Conta conta = findById(contaId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta/Lançamento não encontrado com ID: " + contaId));

        if (conta.getStatus() == StatusConta.PAGO) {
            throw new IllegalStateException("Esta conta já foi paga em " + conta.getDataPagamento());
        }

        conta.setStatus(StatusConta.PAGO);
        conta.setDataPagamento(LocalDate.now());

        return contaRepository.save(conta);
    }

    public List<Conta> findDespesas(Optional<StatusConta> status) {
        User user = getAuthenticatedUser();
        if (status.isPresent()) {
            return contaRepository.findByUserAndTipoAndStatus(user, Tipo.DESPESA, status.get());
        }
        return contaRepository.findByUserAndTipo(user, Tipo.DESPESA);
    }
}