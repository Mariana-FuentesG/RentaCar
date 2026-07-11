package com.prueba.ms_pagos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO {

    private Integer id;

    // ID DE LA RESERVA
    @NotNull(message = "La reserva es obligatoria")
    @Positive(message = "El id de reserva debe ser positivo")
    private Integer reservaId;

    // MONTO DEL PAGO
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    // ESTADO DEL PAGO
    @NotNull(message = "Debe indicar si el pago fue realizado")
    private Boolean pagado;

    // FECHA PAGO
    @NotNull(message = "La fecha de pago es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaPago;

    // METODO PAGO
    @NotBlank(message = "El método de pago es obligatorio")
    @Size(min = 3, max = 50,
            message = "El método de pago debe tener entre 3 y 50 caracteres")
    private String metodoPago;

    // CANTIDAD DE CUOTAS
    @NotNull(message = "La cantidad de cuotas es obligatoria")
    @Positive(message = "Las cuotas deben ser mayores a 0")
    private Integer cantCuotas;

    // DATOS DE RESERVA RELACIONADA
    private Double montoReserva;
    private Boolean reservaPagada;
}
