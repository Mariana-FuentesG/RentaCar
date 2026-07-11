package com.prueba.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(name = "EstadoReserva",
        description = "Representa el estado de una reserva dentro del sistema")
public class EstadoReservaDTO {
    @Schema( description = "Identificador único del estado de reserva",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY )
    private Integer id;

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(min = 3, max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema( description = "Nombre del estado de reserva",
            example = "Pendiente" )
    private String nombreEstado;

    @NotNull(message = "La prioridad es obligatoria")
    @Positive(message = "La prioridad debe ser positiva")
    @Schema( description = "Prioridad del estado", example = "1", minimum = "1" )
    private Integer prioridad;

    @NotNull(message = "El estado activo es obligatorio")
    @Schema( description = "Indica si el estado está habilitado", example = "true" )
    private Boolean activo;

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema( description = "Fecha de creación del estado",
            example = "2026-06-18",
            format = "date" )
    private LocalDate fechaCreacion;

    @NotNull(message = "Los días límite de pago son obligatorios")
    @Positive(message = "Los días límite deben ser positivos")
    @Schema( description = "Cantidad máxima de días permitidos para realizar el pago",
            example = "3",
            minimum = "1" )
    private Integer diasLimitePago;
}