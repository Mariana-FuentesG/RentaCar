package com.prueba.ms_pagos.repository;

import com.prueba.ms_clientes.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    // QUERY METHOD → BUSCAR POR COMUNA
    List<Direccion> findByComunaContainingIgnoreCase(String comuna);

    // QUERY METHOD → BUSCAR POR CIUDAD
    List<Direccion> findByCiudadContainingIgnoreCase(String ciudad);
}