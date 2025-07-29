package com.arctech.os.repositories;

import com.arctech.os.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefone(String telefone);

    Optional<Cliente> findByCpf(String cpf);
}