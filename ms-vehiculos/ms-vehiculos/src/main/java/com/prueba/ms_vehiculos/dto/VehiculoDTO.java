package com.prueba.ms_vehiculos.dto;

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
public class VehiculoDTO {

    private Integer id;

    @NotBlank(message = "La patente es obligatoria")
    @Size(min = 5, max = 20)
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 2, max = 50)
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(min = 2, max = 50)
    private String modelo;

    @NotNull(message = "El precio diario es obligatorio")
    @DecimalMin(value = "0.0")
    private Double precioDiario;

    @NotNull(message = "El año es obligatorio")
    @Positive(message = "El año debe ser positivo")
    private Integer anio;

    private Boolean disponible;

    @NotNull(message = "La fecha ingreso es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaIngreso;

    // FK RELACION
    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "La categoriaId debe ser positiva")
    private Integer categoriaId;

    // DATOS RELACIONADOS
    private String nombreCategoria;
}