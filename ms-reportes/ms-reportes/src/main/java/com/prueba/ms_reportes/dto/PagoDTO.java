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

public class PagoDTO {

    private Integer id;

    private Integer reservaId;

    private Double monto;

    private String metodoPago;

    private Boolean pagado;

    private LocalDate fechaPago;

    private String observacion;
}
