package com.arctech.os.repositories;

import com.arctech.os.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByAtivoTrue();

    List<Servico> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

    List<Servico> findByValorBetweenAndAtivoTrue(Float valorInicial, Float valorFinal);

    List<Servico> findByValorAndAtivoTrue(Float valor);
}