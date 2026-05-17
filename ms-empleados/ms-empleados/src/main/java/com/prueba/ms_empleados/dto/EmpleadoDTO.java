package com.prueba.ms_empleados.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EmpleadoDTO {
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100,
            message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 100,
            message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 8, max = 15,
            message = "El telefono debe tener entre 8 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El cargo es obligatorio")
    @Size(min = 3, max = 80,
            message = "El cargo debe tener entre 3 y 80 caracteres")
    private String cargo;

    @NotNull(message = "El sueldo es obligatorio")
    @Positive(message = "El sueldo debe ser mayor a 0")
    private Double sueldo;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @PastOrPresent(
            message = "La fecha de contratación no puede ser futura")
    private LocalDate fechaContratacion;
}
