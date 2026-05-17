package com.prueba.ms_pagos.repository;

import com.prueba.ms_pagos.model.Pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PagoRepository
        extends JpaRepository<Pago, Integer> {

    // JPQL → PAGOS POR RANGO DE MONTO
    @Query("""
            SELECT p
            FROM Pago p
            WHERE p.monto
            BETWEEN :minimo AND :maximo
            ORDER BY p.fechaPago DESC
            """)
    List<Pago> buscarPagosPorMonto(
            @Param("minimo") Double minimo,
            @Param("maximo") Double maximo);
}