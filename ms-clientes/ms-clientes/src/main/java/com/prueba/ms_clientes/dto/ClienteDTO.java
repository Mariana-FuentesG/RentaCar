package com.prueba.ms_clientes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Cliente", description = "Representa un cliente del sistema RentaCar")
public class ClienteDTO {

    @Schema(description = "Identificador único del cliente",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotNull(message = "El rut es obligatorio")
    @Schema(description = "RUT del cliente sin puntos ni dígito verificador",
            example = "12345678")
    private Integer rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    @Schema(description = "Nombre completo del cliente",
            example = "Juan Pérez")
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Correo Inválido")
    @Schema(description = "Correo electrónico del cliente",
            example = "juan@example.com")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 2, max = 10,
            message = "El telefono debe tener entre 2 y 10 caracteres")
    @Schema(description = "Número de teléfono del cliente",
            example = "912345678")
    private String telefono;

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Indica si el cliente está activo en el sistema",
            example = "true")
    private Boolean activo;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Schema(description = "Fecha de registro del cliente",
            example = "2026-06-20", format = "date")
    private LocalDate fechaRegistro;

    @Schema(description = "Lista de direcciones asociadas al cliente",
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<DireccionDTO> direcciones;
}