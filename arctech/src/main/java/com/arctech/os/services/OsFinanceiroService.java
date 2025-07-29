package com.arctech.os.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.financial.entities.Conta;
import com.arctech.financial.entities.Instituicao;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.enums.Tipo;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.repositories.InstituicaoRepository;
import com.arctech.os.dto.FinalizarOsRequestDto;
import com.arctech.os.entities.OrdemServico;
import com.arctech.os.enums.StatusOS;
import com.arctech.os.repositories.OrdemServicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OsFinanceiroService {

    @Autowired
    private OrdemServicoRepository osRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    private static Conta getContaReceita(FinalizarOsRequestDto dto, OrdemServico os, Instituicao instituicaoReceita) {
        float valorTotalReceita = os.getServicoPrestado().getValor() + (os.getCustos() != null ? os.getCustos() : 0.0f);
        Conta contaReceita = new Conta();
        contaReceita.setDescricao("Receita referente à OS #" + os.getId());
        contaReceita.setValor(valorTotalReceita);
        contaReceita.setTipo(Tipo.RECEITA);
        contaReceita.setStatus(StatusConta.PAGO);
        contaReceita.setDataVencimento(dto.getDataRecebimento());
        contaReceita.setDataPagamento(dto.getDataRecebimento());
        contaReceita.setInstituicao(instituicaoReceita);
        contaReceita.setUser(os.getTecnico()); // A receita é do usuário (técnico) que realizou o serviço
        contaReceita.setOrdemServico(os);
        return contaReceita;
    }

    private static Conta getConta(FinalizarOsRequestDto dto, OrdemServico os, Instituicao instituicaoReceita) {
        Conta contaDespesa = new Conta();
        contaDespesa.setDescricao("Custos/Peças para a OS #" + os.getId());
        contaDespesa.setValor(os.getCustos());
        contaDespesa.setTipo(Tipo.DESPESA);
        contaDespesa.setStatus(StatusConta.PAGO);
        contaDespesa.setDataVencimento(dto.getDataRecebimento());
        contaDespesa.setDataPagamento(dto.getDataRecebimento());
        // Assumindo que o custo sai da mesma instituição que a receita entrou. Pode ser alterado.
        contaDespesa.setInstituicao(instituicaoReceita);
        contaDespesa.setUser(os.getTecnico());
        contaDespesa.setOrdemServico(os);
        return contaDespesa;
    }

    @Transactional
    public void lancarFinanceiro(Long osId, FinalizarOsRequestDto dto) {
        OrdemServico os = osRepository.findById(osId)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com ID: " + osId));

        if (os.getStatus() != StatusOS.FINALIZADA) {
            throw new ValidationException("Apenas Ordens de Serviço com status FINALIZADA podem ter o financeiro lançado.");
        }
        if (os.isFinanceiroLancado()) {
            throw new ValidationException("O financeiro para esta Ordem de Serviço já foi lançado.");
        }

        Instituicao instituicaoReceita = instituicaoRepository.findById(dto.getInstituicaoReceitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição de receita não encontrada com ID: " + dto.getInstituicaoReceitaId()));

        Conta contaReceita = getContaReceita(dto, os, instituicaoReceita);
        contaRepository.save(contaReceita);

        if (os.getCustos() != null && os.getCustos() > 0) {
            Conta contaDespesa = getConta(dto, os, instituicaoReceita);
            contaRepository.save(contaDespesa);
        }

        os.setFinanceiroLancado(true);
        osRepository.save(os);
    }
}