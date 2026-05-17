package com.prueba.ms_reportes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="REPORTES")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 300)
    private String descripcion;

    @Column(nullable = false)
    private Integer totalReservas;

    @Column(nullable = false)
    private Double totalIngresos;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private LocalDate fechaGeneracion;
}
