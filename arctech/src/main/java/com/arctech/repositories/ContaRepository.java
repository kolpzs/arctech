package com.arctech.repositories;

import com.arctech.entities.Conta;
import com.arctech.entities.Tipo;
import com.arctech.entities.User; // <-- IMPORTAR
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {


    List<Conta> findByUser(User user);

    Optional<Conta> findByIdAndUser(Long id, User user);

    List<Conta> findByDataBetweenAndUser(Date dataInicio, Date dataFim, User user);

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.tipo = :tipo AND c.user = :user")
    Optional<Float> sumByTipoAndUser(@Param("tipo") Tipo tipo, @Param("user") User user);

    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.tipo = :tipo AND c.data BETWEEN :dataInicio AND :dataFim AND c.user = :user")
    Optional<Float> sumByTipoAndDataBetweenAndUser(@Param("tipo") Tipo tipo, @Param("dataInicio") Date dataInicio, @Param("dataFim") Date dataFim, @Param("user") User user);
}