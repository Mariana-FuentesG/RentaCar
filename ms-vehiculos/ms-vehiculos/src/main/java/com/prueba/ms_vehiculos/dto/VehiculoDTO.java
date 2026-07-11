package com.prueba.ms_vehiculos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Vehiculo", description = "Representa un vehículo disponible en RentaCar")
public class VehiculoDTO {

    @Schema(description = "Identificador único del vehículo",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "La patente es obligatoria")
    @Size(min = 5, max = 20)
    @Schema(description = "Patente del vehículo",
            example = "ABCD12")
    private String patente;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 2, max = 50)
    @Schema(description = "Marca del vehículo",
            example = "Toyota")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(min = 2, max = 50)
    @Schema(description = "Modelo del vehículo",
            example = "Corolla")
    private String modelo;

    @NotNull(message = "El precio diario es obligatorio")
    @DecimalMin(value = "0.0")
    @Schema(description = "Precio de arriendo por día",
            example = "50000.0")
    private Double precioDiario;

    @NotNull(message = "El año es obligatorio")
    @Positive(message = "El año debe ser positivo")
    @Schema(description = "Año de fabricación del vehículo",
            example = "2023")
    private Integer anio;

    @Schema(description = "Indica si el vehículo está disponible para arriendo",
            example = "true")
    private Boolean disponible;

    @NotNull(message = "La fecha ingreso es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema(description = "Fecha de ingreso del vehículo al sistema",
            example = "2026-06-20", format = "date")
    private LocalDate fechaIngreso;

    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "La categoriaId debe ser positiva")
    @Schema(description = "ID de la categoría del vehículo",
            example = "1")
    private Integer categoriaId;

    @Schema(description = "Nombre de la categoría del vehículo",
            example = "SUV", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreCategoria;
}