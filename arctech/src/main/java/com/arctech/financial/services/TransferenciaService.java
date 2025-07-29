package com.arctech.financial.services;

import com.arctech.exceptions.ResourceNotFoundException;
import com.arctech.financial.dto.TransferenciaDto;
import com.arctech.financial.entities.Categoria;
import com.arctech.financial.entities.Conta;
import com.arctech.financial.entities.Instituicao;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.enums.Tipo;
import com.arctech.financial.enums.TipoCategoria;
import com.arctech.financial.repositories.CategoriaRepository;
import com.arctech.financial.repositories.ContaRepository;
import com.arctech.financial.repositories.InstituicaoRepository;
import com.arctech.users.entities.User;
import com.arctech.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferenciaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository; // Adicionar injeção

    @Autowired
    private ContaService contaService;

    @Autowired
    private UserService userService;

    // Constante para o nome da categoria padrão
    private static final String NOME_CATEGORIA_TRANSFERENCIA = "Transferência entre Contas";

    @Transactional
    public void realizarTransferencia(TransferenciaDto dto) {
        if (dto.getIdInstituicaoOrigem().equals(dto.getIdInstituicaoDestino())) {
            throw new IllegalArgumentException("A instituição de origem e destino não podem ser a mesma.");
        }

        Jwt jwt = contaService.getAuthenticatedUserAsJwt();
        User user = userService.findOrCreateUserFromJwt(jwt);

        // LÓGICA AUTOMÁTICA PARA GARANTIR A CATEGORIA
        Categoria categoriaTransferencia = findOrCreateCategoriaTransferencia();

        Instituicao origem = instituicaoRepository.findById(dto.getIdInstituicaoOrigem())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição de origem não encontrada com ID: " + dto.getIdInstituicaoOrigem()));

        Instituicao destino = instituicaoRepository.findById(dto.getIdInstituicaoDestino())
                .orElseThrow(() -> new ResourceNotFoundException("Instituição de destino não encontrada com ID: " + dto.getIdInstituicaoDestino()));

        // Criando a conta de SAÍDA (débito)
        Conta saida = new Conta();
        saida.setDescricao("Transferência para " + destino.getNome() + (dto.getDescricao() != null ? " - " + dto.getDescricao() : ""));
        saida.setValor(dto.getValor());
        saida.setTipo(Tipo.DESPESA);
        saida.setStatus(StatusConta.PAGO);
        saida.setDataVencimento(dto.getData());
        saida.setDataPagamento(dto.getData());
        saida.setInstituicao(origem);
        saida.setUser(user);
        saida.setCategoria(categoriaTransferencia); // Associando a categoria correta

        // Criando a conta de ENTRADA (crédito)
        Conta entrada = new Conta();
        entrada.setDescricao("Transferência de " + origem.getNome() + (dto.getDescricao() != null ? " - " + dto.getDescricao() : ""));
        entrada.setValor(dto.getValor());
        entrada.setTipo(Tipo.RECEITA);
        entrada.setStatus(StatusConta.PAGO);
        entrada.setDataVencimento(dto.getData());
        entrada.setDataPagamento(dto.getData());
        entrada.setInstituicao(destino);
        entrada.setUser(user);
        entrada.setCategoria(categoriaTransferencia); // Associando a categoria correta

        contaRepository.save(saida);
        contaRepository.save(entrada);
    }

    private Categoria findOrCreateCategoriaTransferencia() {
        return categoriaRepository.findByNome(NOME_CATEGORIA_TRANSFERENCIA)
                .orElseGet(() -> {
                    Categoria novaCategoria = new Categoria();
                    novaCategoria.setNome(NOME_CATEGORIA_TRANSFERENCIA);
                    novaCategoria.setTipoCategoria(TipoCategoria.TRANSFERENCIA); // Define o tipo correto
                    return categoriaRepository.save(novaCategoria);
                });
    }
}