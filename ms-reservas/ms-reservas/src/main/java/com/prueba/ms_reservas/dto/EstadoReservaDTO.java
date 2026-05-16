package com.prueba.ms_reservas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoReservaDTO {

    private Integer id;

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotNull(message = "La prioridad es obligatoria")
    @Positive(message = "La prioridad debe ser positiva")
    private Integer prioridad;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaCreacion;

    @NotNull(message = "Los días límite de pago son obligatorios")
    @Positive(message = "Los días límite deben ser positivos")
    private Integer diasLimitePago;
}