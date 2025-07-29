package com.arctech.financial.services;

import com.arctech.financial.entities.Conta;
import com.arctech.financial.entities.ContaRecorrente;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.repositories.ContaRecorrenteRepository;
import com.arctech.financial.repositories.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaRecorrenteService {

    @Autowired
    private ContaRecorrenteRepository recorrenteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * ?")
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
}