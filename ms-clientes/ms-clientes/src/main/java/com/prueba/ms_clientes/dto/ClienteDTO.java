package com.prueba.ms_clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClienteDTO {

    private Integer id;
    private String nombreCompleto;
    private Integer rut;
    private String email;
    private String telefono;
    private Boolean activo;
    private LocalDate fechaRegistro;

    private List<DireccionDTO> direcciones;
}
