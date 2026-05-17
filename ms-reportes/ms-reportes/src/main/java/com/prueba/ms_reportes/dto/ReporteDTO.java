package com.prueba.ms_reportes.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReporteDTO {
    private Integer id;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 3, max = 150,
            message = "El titulo debe tener entre 3 y 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 5, max = 300,
            message = "La descripcion debe tener entre 5 y 300 caracteres")
    private String descripcion;

    @NotNull(message = "El total de reservas es obligatorio")
    @Positive(message = "El total de reservas debe ser mayor a 0")
    private Integer totalReservas;

    @NotNull(message = "El total de ingresos es obligatorio")
    @Positive(message = "El total de ingresos debe ser mayor a 0")
    private Double totalIngresos;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;

    @NotNull(message = "La fecha de generacion es obligatoria")
    @PastOrPresent(
            message = "La fecha de generacion no puede ser futura")
    private LocalDate fechaGeneracion;
}
