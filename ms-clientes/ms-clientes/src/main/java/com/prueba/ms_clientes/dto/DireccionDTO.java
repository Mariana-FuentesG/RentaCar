package com.prueba.ms_clientes.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {

    private Integer id;
    private String calle;
    private String comuna;
    private Integer numeroCasa;
    private Integer codigoPostal;
    private Boolean estado;
    private LocalDate fechaRegistro;

    //Relación con cliente
    private Integer clienteId;
    private String nombreCliente;
}
