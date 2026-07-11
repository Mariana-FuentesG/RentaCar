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
@Schema(name = "Categoria", description = "Representa una categoría de vehículos en RentaCar")
public class CategoriaDTO {

    @Schema(description = "Identificador único de la categoría",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50)
    @Schema(description = "Nombre de la categoría",
            example = "SUV")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    @Schema(description = "Capacidad máxima de pasajeros",
            example = "5")
    private Integer capacidadPasajeros;

    @Schema(description = "Indica si la categoría está activa",
            example = "true")
    private Boolean activa;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema(description = "Fecha de creación de la categoría",
            example = "2026-06-20", format = "date")
    private LocalDate fechaCreacion;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0")
    @Schema(description = "Precio base diario de la categoría",
            example = "80000.0")
    private Double precioBase;
}