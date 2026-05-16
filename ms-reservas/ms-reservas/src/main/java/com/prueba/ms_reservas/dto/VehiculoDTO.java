package com.prueba.ms_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoDTO {

    private Integer id;

    private String patente;

    private String marca;

    private String modelo;

    private Integer anio;

    private Boolean disponible;
}