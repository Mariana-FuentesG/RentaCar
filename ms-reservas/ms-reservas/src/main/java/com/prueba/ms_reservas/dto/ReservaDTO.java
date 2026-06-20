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

@Schema( name = "Reserva",
        description = "Representa una reserva realizada por un cliente para un vehículo" )

public class ReservaDTO {
    @Schema(description = "Identificador único de la reserva",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY )
    private Integer id;

    @NotNull(message = "El cliente es obligatorio")
    @Positive(message = "El id del cliente debe ser positivo")
    @Schema(description = "Identificador del cliente",
            example = "10",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer clienteId;

    @Schema(description = "Nombre del cliente (obtenido desde otro microservicio)",
            example = "Mariana Fuentes",
            accessMode = Schema.AccessMode.READ_ONLY )
    private String nombreCliente;

    @Schema(description = "Marca del vehículo reservado",
            example = "Peugeot",
            accessMode = Schema.AccessMode.READ_ONLY )
    private String marcaVehiculo;

    @Schema(description = "Modelo del vehículo reservado",
            example = "2008",
            accessMode = Schema.AccessMode.READ_ONLY )
    private String modeloVehiculo;

    @NotNull(message = "El vehículo es obligatorio")
    @Positive(message = "El id del vehículo debe ser positivo")
    @Schema(description = "Identificador del vehículo reservado",
            example = "25",
            minimum = "1" )
    private Integer vehiculoId;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0",
            message = "El monto debe ser mayor o igual a 0")
    @Schema( description = "Monto total de la reserva",
            example = "250000" )
    private Double montoReserva;

    @NotNull(message = "La cantidad de días es obligatoria")
    @Positive(message = "La cantidad de días debe ser positiva")
    @Schema( description = "Cantidad de días reservados",
            example = "5",
            minimum = "1" )
    private Integer cantidadDias;

    @NotNull(message = "El estado de pago es obligatorio")
    @Schema(description = "Indica si la reserva fue pagada",
            example = "false")
    private Boolean pagada;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent
    @Schema(description = "Fecha de inicio de la reserva",
            example = "2026-06-20",
            format = "date" )
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    @FutureOrPresent
    @Schema(description = "Fecha de término de la reserva",
            example = "2026-06-25",
            format = "date" )
    private LocalDate fechaTermino;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @PastOrPresent
    @Schema( description = "Fecha en que se registró la reserva",
            example = "2026-06-18",
            format = "date" )
    private LocalDate fechaReserva;

    @NotBlank(message = "La observación es obligatoria")
    @Size(min = 5, max = 200)
    @Schema( description = "Observaciones adicionales",
            example = "Cliente solicita entrega en sucursal central" )
    private String observacion;

    // DATOS DEL ESTADO RELACIONADO
    @NotNull(message = "El estado de reserva es obligatorio")
    @Schema( description = "Identificador del estado de reserva",
            example = "1" )
    private Integer estadoReservaId;
    @Schema( description = "Nombre del estado asociado",
            example = "Pendiente",
            accessMode = Schema.AccessMode.READ_ONLY )
    private String nombreEstado;
}
