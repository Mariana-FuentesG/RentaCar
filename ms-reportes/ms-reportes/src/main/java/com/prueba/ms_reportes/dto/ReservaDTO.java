package com.prueba.ms_reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaDTO {
    private Integer id;
    private Integer clienteId;
    private Integer vehiculoId;
    private Double montoReserva;
    private Integer cantidadDias;
    private Boolean pagada;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private LocalDate fechaReserva;
    private String observacion;
    private Integer estadoReservaId;
    private String estadoReservaNombre;
}
