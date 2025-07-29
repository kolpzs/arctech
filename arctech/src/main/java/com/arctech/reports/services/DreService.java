package com.arctech.reports.services;

import com.arctech.financial.entities.Conta;
import com.arctech.financial.enums.Tipo;
import com.arctech.financial.enums.TipoCategoria;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.services.ContaService;
import com.arctech.reports.dto.DreDto;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DreService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private UserService userService;

    public DreDto gerarDre(LocalDate dataInicio, LocalDate dataFim) {
        Jwt jwt = contaService.getAuthenticatedUserAsJwt();
        User user = userService.findOrCreateUserFromJwt(jwt);

        // 1. Calcular Receita Bruta (continua igual)
        Float receitaBruta = contaRepository.sumByTipoAndDataVencimentoBetween(Tipo.RECEITA, user, dataInicio, dataFim).orElse(0.0f);

        // 2. Buscar TODAS as despesas do período de uma só vez.
        List<Conta> todasAsDespesas = contaRepository.findByUserAndTipoAndDataVencimentoBetween(user, Tipo.DESPESA, dataInicio, dataFim);

        // 3. Separar Custos e Despesas Operacionais, IGNORANDO TRANSFERÊNCIAS
        Float custos = todasAsDespesas.stream()
                .filter(c -> c.getCategoria() != null && c.getCategoria().getTipoCategoria() == TipoCategoria.CUSTO)
                .map(Conta::getValor)
                .reduce(0.0f, Float::sum);

        Float despesasOperacionais = todasAsDespesas.stream()
                .filter(c -> c.getCategoria() != null && c.getCategoria().getTipoCategoria() == TipoCategoria.DESPESA_OPERACIONAL)
                .map(Conta::getValor)
                .reduce(0.0f, Float::sum);

        // O restante do método continua igual...
        Float lucroBruto = receitaBruta - custos;
        Float lucroLiquido = lucroBruto - despesasOperacionais;

        return DreDto.builder()
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .receitaBruta(receitaBruta)
                .custos(custos)
                .lucroBruto(lucroBruto)
                .despesasOperacionais(despesasOperacionais)
                .lucroLiquido(lucroLiquido)
                .build();
    }
}