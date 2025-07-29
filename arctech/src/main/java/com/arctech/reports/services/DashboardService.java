package com.arctech.reports.services;

import com.arctech.financial.dto.ContaResponseDto;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.enums.Tipo;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.services.ContaService;
import com.arctech.reports.dto.DashboardDto;
import com.arctech.reports.dto.RelatorioOsMensalDto;
import com.arctech.reports.dto.SaldoPorInstituicaoDto;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private UserService userService;

    @Autowired
    private RelatorioOsService relatorioOsService;

    public DashboardDto getDashboardData() {
        User user = userService.findOrCreateUserFromJwt(contaService.getAuthenticatedUserAsJwt());

        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = hoje.with(TemporalAdjusters.lastDayOfMonth());

        Float saldoAtual = contaService.getSaldoAtual();

        Float receitasMes = contaRepository.sumByTipoAndDataVencimentoBetween(Tipo.RECEITA, user, inicioMes, fimMes).orElse(0.0f);
        Float despesasMes = contaRepository.sumByTipoAndDataVencimentoBetween(Tipo.DESPESA, user, inicioMes, fimMes).orElse(0.0f);

        var contasPagas = contaRepository.findByUserAndStatusAndDataVencimentoBetween(user, StatusConta.PAGO, inicioMes, fimMes)
                .stream().map(ContaResponseDto::new).collect(Collectors.toList());

        var contasPendentes = contaRepository.findByUserAndStatusAndDataVencimentoBetween(user, StatusConta.PENDENTE, inicioMes, fimMes)
                .stream().map(ContaResponseDto::new).collect(Collectors.toList());

        var proximasVencer = contaRepository.findProximasContasVencer(user, hoje)
                .stream().map(ContaResponseDto::new).collect(Collectors.toList());

        RelatorioOsMensalDto relatorioOs = relatorioOsService.gerarRelatorioMensal(hoje.getYear(), hoje.getMonthValue());

        return DashboardDto.builder()
                .saldoAtual(saldoAtual)
                .totalReceitasMes(receitasMes)
                .totalDespesasMes(despesasMes)
                .contasPagasMes(contasPagas)
                .contasPendentesMes(contasPendentes)
                .proximasContasVencer(proximasVencer)
                .osFinalizadasMes(relatorioOs.getQuantidadeOsFinalizadas())
                .lucroOsMes(relatorioOs.getLucroTotalOs())
                .build();
    }

    public List<SaldoPorInstituicaoDto> getSaldoPorInstituicao() {
        User user = userService.findOrCreateUserFromJwt(contaService.getAuthenticatedUserAsJwt());
        return contaRepository.findSaldoAtualPorInstituicao(user);
    }
}