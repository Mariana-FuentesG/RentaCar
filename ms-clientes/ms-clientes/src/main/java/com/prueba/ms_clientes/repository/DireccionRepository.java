package com.prueba.ms_clientes.repository;

import com.prueba.ms_clientes.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Integer>
{
}
