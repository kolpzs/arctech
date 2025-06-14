package com.arctech.services;

import com.arctech.entities.Conta;
import com.arctech.entities.Tipo;
import com.arctech.entities.User;
import com.arctech.repositories.ContaRepository;
import com.arctech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User getAuthenticatedUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.synchronizeUser(jwt);
    }

    public Conta save(Conta conta) {
        conta.setUser(getAuthenticatedUser());
        return contaRepository.save(conta);
    }

    public List<Conta> findAll() {
        return contaRepository.findByUser(getAuthenticatedUser());
    }

    public Optional<Conta> findById(Long id) {
        return contaRepository.findByIdAndUser(id, getAuthenticatedUser());
    }

    public Conta update(Long id, Conta contaAtualizada) {
        return findById(id).map(contaExistente -> {
            contaExistente.setData(contaAtualizada.getData());
            contaExistente.setDescricao(contaAtualizada.getDescricao());
            contaExistente.setValor(contaAtualizada.getValor());
            contaExistente.setTipo(contaAtualizada.getTipo());
            contaExistente.setCategoria(contaAtualizada.getCategoria());
            contaExistente.setInstituicao(contaAtualizada.getInstituicao());
            return contaRepository.save(contaExistente);
        }).orElseThrow(() -> new RuntimeException("Conta com ID " + id + " não encontrada para este usuário."));
    }

    public void deleteById(Long id) {
        if (!findById(id).isPresent()) {
            throw new RuntimeException("Conta com ID " + id + " não encontrada para este usuário.");
        }
        contaRepository.deleteById(id);
    }

    public List<Conta> findContasByPeriodo(Date dataInicio, Date dataFim) {
        return contaRepository.findByDataBetweenAndUser(dataInicio, dataFim, getAuthenticatedUser());
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

    public Float getTotalReceitasPorPeriodo(Date dataInicio, Date dataFim) {
        return contaRepository.sumByTipoAndDataBetweenAndUser(Tipo.RECEITA, dataInicio, dataFim, getAuthenticatedUser()).orElse(0.0f);
    }

    public Float getTotalDespesasPorPeriodo(Date dataInicio, Date dataFim) {
        return contaRepository.sumByTipoAndDataBetweenAndUser(Tipo.DESPESA, dataInicio, dataFim, getAuthenticatedUser()).orElse(0.0f);
    }
}