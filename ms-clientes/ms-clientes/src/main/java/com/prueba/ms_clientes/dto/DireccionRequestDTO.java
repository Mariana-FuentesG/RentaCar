package com.prueba.ms_clientes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionRequestDTO {

    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 2, max = 100, message = "La calle debe tener entre 2 y 100 caracteres")
    private String calle;

    @NotBlank(message = "La comuna es obligatoria")
    @Size(min = 2, max = 100, message = "La comuna debe tener entre 2 y 100 caracteres")
    private String comuna;

    @NotNull(message = "El número de casa es obligatorio")
    @Positive(message = "El número de casa debe ser positivo")
    private Integer numeroCasa;

    @NotNull(message = "El código postal es obligatorio")
    @Positive(message = "El código postal debe ser positivo")
    private Integer codigoPostal;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaRegistro;

    @NotNull(message = "El cliente es obligatorio")
    private Integer clienteId;
}
