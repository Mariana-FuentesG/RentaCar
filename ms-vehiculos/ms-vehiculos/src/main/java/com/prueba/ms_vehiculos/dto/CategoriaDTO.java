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
public class CategoriaDTO {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50)
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    private Integer capacidadPasajeros;

    private Boolean activa;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaCreacion;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0")
    private Double precioBase;
}
