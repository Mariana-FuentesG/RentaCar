package com.prueba.ms_pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {

    // ID DE LA RESERVA
    private Integer id;
    // ID DEL CLIENTE
    private Integer clienteId;
    // ID DEL VEHICULO
    private Integer vehiculoId;
    // MONTO TOTAL DE LA RESERVA
    private Double montoReserva;
    // CANTIDAD DE DIAS RESERVADOS
    private Integer cantidadDias;
    // INDICA SI LA RESERVA ESTA PAGADA
    private Boolean pagada;
    // FECHA DE INICIO DE LA RESERVA
    private LocalDate fechaInicio;
    // FECHA DE TERMINO DE LA RESERVA
    private LocalDate fechaTermino;
    // FECHA EN QUE SE REALIZO LA RESERVA
    private LocalDate fechaReserva;
    // OBSERVACION O COMENTARIO
    private String observacion;
    // ID DEL ESTADO DE RESERVA
    private Integer estadoReservaId;
    // NOMBRE DEL ESTADO
    private String nombreEstado;
}
