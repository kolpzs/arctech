package com.arctech.financial.repositories;

import com.arctech.financial.entities.Conta;
import com.arctech.financial.entities.ContaRecorrente;
import com.arctech.financial.enums.StatusConta;
import com.arctech.financial.enums.Tipo;
import com.arctech.reports.dto.LucroPorOsDto;
import com.arctech.reports.dto.SaldoPorInstituicaoDto;
import com.arctech.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByUser(User user);

    Optional<Conta> findByIdAndUser(Long id, User user);

    List<Conta> findByUserAndDataVencimentoBetweenOrderByDataVencimentoAsc(User user, LocalDate inicio, LocalDate fim);

    List<Conta> findByUserAndTipoAndDataVencimentoBetween(User user, Tipo tipo, LocalDate inicio, LocalDate fim);

    List<Conta> findByUserAndTipo(User user, Tipo tipo);

    List<Conta> findByUserAndTipoAndStatus(User user, Tipo tipo, StatusConta status);

    @Query("SELECT c FROM Conta c WHERE c.user = :user AND c.status = :status AND c.dataVencimento BETWEEN :inicioMes AND :fimMes")
    List<Conta> findByUserAndStatusAndDataVencimentoBetween(@Param("user") User user, @Param("status") StatusConta status, @Param("inicioMes") LocalDate inicioMes, @Param("fimMes") LocalDate fimMes);

    @Query("SELECT c FROM Conta c WHERE c.user = :user AND c.status = 'PENDENTE' AND c.dataVencimento >= :hoje ORDER BY c.dataVencimento ASC")
    List<Conta> findProximasContasVencer(@Param("user") User user, @Param("hoje") LocalDate hoje);

    @Query("SELECT new com.arctech.reports.dto.SaldoPorInstituicaoDto(c.instituicao.nome, SUM(CASE WHEN c.tipo = 'RECEITA' THEN c.valor ELSE -c.valor END)) " +
            "FROM com.arctech.financial.entities.Conta c WHERE c.user = :user AND c.status = 'PAGO' " +
            "GROUP BY c.instituicao.nome")
    List<SaldoPorInstituicaoDto> findSaldoAtualPorInstituicao(@Param("user") User user);

    @Query("SELECT SUM(c.valor) FROM com.arctech.financial.entities.Conta c WHERE c.tipo = :tipo AND c.user = :user")
    Optional<Float> sumByTipoAndUser(@Param("tipo") Tipo tipo, @Param("user") User user);

    @Query("SELECT SUM(c.valor) FROM com.arctech.financial.entities.Conta c WHERE c.tipo = :tipo AND c.user = :user AND c.dataVencimento BETWEEN :inicioMes AND :fimMes")
    Optional<Float> sumByTipoAndDataVencimentoBetween(@Param("tipo") Tipo tipo, @Param("user") User user, @Param("inicioMes") LocalDate inicioMes, @Param("fimMes") LocalDate fimMes);

    @Query("SELECT COALESCE(SUM(CASE WHEN c.tipo = 'RECEITA' THEN c.valor ELSE -c.valor END), 0.0) FROM com.arctech.financial.entities.Conta c WHERE c.user = :user AND c.dataVencimento < :data")
    Float findSaldoAteData(@Param("user") User user, @Param("data") LocalDate data);

    boolean existsByContaRecorrenteAndDataVencimento(ContaRecorrente contaRecorrente, LocalDate dataVencimento);

    @Query("SELECT new com.arctech.reports.dto.LucroPorOsDto(" +
            "os.id, " +
            "os.cliente.nome, " +
            "os.dataFinalizacao, " +
            "SUM(CASE WHEN c.tipo = 'RECEITA' THEN c.valor ELSE 0 END), " +
            "SUM(CASE WHEN c.tipo = 'DESPESA' THEN c.valor ELSE 0 END), " +
            "SUM(CASE WHEN c.tipo = 'RECEITA' THEN c.valor ELSE -c.valor END)) " +
            "FROM Conta c JOIN c.ordemServico os " +
            "WHERE c.status = 'PAGO' AND os.status = 'FINALIZADA' AND " +
            "FUNCTION('YEAR', c.dataPagamento) = :ano AND FUNCTION('MONTH', c.dataPagamento) = :mes " +
            "GROUP BY os.id, os.cliente.nome, os.dataFinalizacao")
    List<LucroPorOsDto> findLucroPorOsNoPeriodo(@Param("ano") int ano, @Param("mes") int mes);
}