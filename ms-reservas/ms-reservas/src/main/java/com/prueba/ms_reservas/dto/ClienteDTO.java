package com.prueba.ms_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Integer id;
    private Integer rut;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Boolean activo;
    private LocalDate fechaRegistro;
}
