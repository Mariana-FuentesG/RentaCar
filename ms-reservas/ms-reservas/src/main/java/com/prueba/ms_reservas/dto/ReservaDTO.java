package com.prueba.ms_reservas.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservaDTO {
    private Integer id;

    @NotNull(message = "El cliente es obligatorio")
    @Positive(message = "El id del cliente debe ser positivo")
    private Integer clienteId;

    @NotNull(message = "El vehículo es obligatorio")
    @Positive(message = "El id del vehículo debe ser positivo")
    private Integer vehiculoId;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0",
            message = "El monto debe ser mayor o igual a 0")
    private Double montoTotal;

    @NotNull(message = "La cantidad de días es obligatoria")
    @Positive(message = "La cantidad de días debe ser positiva")
    private Integer cantidadDias;

    @NotNull(message = "El estado de pago es obligatorio")
    private boolean pagada;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    @FutureOrPresent
    private LocalDate fechaTermino;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @PastOrPresent
    private LocalDate fechaReserva;

    @NotBlank(message = "La observación es obligatoria")
    @Size(min = 5, max = 200)
    private String observacion;

    // DATOS DEL ESTADO RELACIONADO
    @NotNull(message = "El estado de reserva es obligatorio")
    private Integer estadoReservaId;
    private String nombreEstado;
}
