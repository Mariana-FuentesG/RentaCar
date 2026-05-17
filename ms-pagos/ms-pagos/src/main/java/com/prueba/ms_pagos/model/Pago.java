package com.prueba.ms_pagos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PAGOS")

public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer reservaId;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private Boolean pagado;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private Integer cantCuotas;
}
