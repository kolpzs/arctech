package com.arctech.financial.repositories;

import com.arctech.financial.entities.ContaRecorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContaRecorrenteRepository extends JpaRepository<ContaRecorrente, Long> {

    @Query("SELECT cr FROM ContaRecorrente cr WHERE cr.dataInicio <= :data AND (cr.dataFim IS NULL OR cr.dataFim >= :data)")
    List<ContaRecorrente> findRecorrenciasAtivasEm(@Param("data") LocalDate data);
}