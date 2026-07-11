package com.prueba.ms_clientes.dto;

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
@Schema(name = "Direccion", description = "Representa una dirección asociada a un cliente")
public class DireccionDTO {

    @Schema(description = "Identificador único de la dirección",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 2, max = 100, message = "La calle debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre de la calle",
            example = "Av. Providencia")
    private String calle;

    @NotNull(message = "El número de casa es obligatorio")
    @Positive(message = "El número de casa debe ser positivo")
    @Schema(description = "Número de la casa o departamento",
            example = "1234")
    private Integer numeroCasa;

    @NotBlank(message = "La comuna es obligatoria")
    @Size(min = 2, max = 100, message = "La comuna debe tener entre 2 y 100 caracteres")
    @Schema(description = "Comuna de la dirección",
            example = "Providencia")
    private String comuna;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 2, max = 100, message = "La ciudad debe tener entre 2 y 100 caracteres")
    @Schema(description = "Ciudad de la dirección",
            example = "Santiago")
    private String ciudad;

    @NotNull(message = "El código postal es obligatorio")
    @Positive(message = "El código postal debe ser positivo")
    @Schema(description = "Código postal de la dirección",
            example = "7500000")
    private Integer codigoPostal;

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Indica si la dirección está activa",
            example = "true")
    private Boolean estado;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema(description = "Fecha de registro de la dirección",
            example = "2026-06-20", format = "date")
    private LocalDate fechaRegistro;

    @Schema(description = "ID del cliente asociado a esta dirección",
            example = "1")
    private Integer clienteId;

    @Schema(description = "Nombre completo del cliente asociado",
            example = "Juan Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreCliente;
}