package com.arctech.os.repositories;

import com.arctech.os.entities.Cliente;
import com.arctech.os.entities.OrdemServico;
import com.arctech.os.enums.StatusOS;
import com.arctech.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findByCliente(Cliente cliente);

    List<OrdemServico> findByTecnico(User tecnico);

    List<OrdemServico> findByStatusOrderByDataAberturaDesc(StatusOS status);

    @Query("SELECT os FROM OrdemServico os WHERE os.dataEstipulada < :hoje AND os.status IN :statusAbertos")
    List<OrdemServico> findAtrasadas(LocalDate hoje, List<StatusOS> statusAbertos);

    long countByDataFinalizacaoBetween(LocalDateTime start, LocalDateTime end);
}