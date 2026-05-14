package com.prueba.ms_clientes.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteRequestDTO {

    //private Integer id; - preguntar al profe

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min =2, max = 100)
    private String nombreCompleto;

    @NotNull(message = "El rut es obligatorio")
    private Integer rut;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Correo Inválido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min =2, max = 10, message = "El telefono debe tener entre 2 y 10 caracteres")
    private String telefono;

    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;

    @NotNull (message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaRegistro;

}
