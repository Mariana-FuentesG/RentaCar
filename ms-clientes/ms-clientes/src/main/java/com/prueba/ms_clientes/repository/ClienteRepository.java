package com.prueba.ms_clientes.repository;

import com.prueba.ms_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
        List<Cliente> findByEmailContainingIgnoreCase(String email);

    long deleteByEmail(String email);
}
