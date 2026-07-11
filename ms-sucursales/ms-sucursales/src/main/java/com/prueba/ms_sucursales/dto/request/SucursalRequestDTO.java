package com.prueba.ms_sucursales.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada. No incluye id: el identificador lo asigna la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(min = 5, max = 150, message = "La direccion debe tener entre 5 y 150 caracteres")
    private String direccion;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 8, max = 15, message = "El telefono debe tener entre 8 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 3, max = 50, message = "La ciudad debe tener entre 3 y 50 caracteres")
    private String ciudad;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activa;

    @NotNull(message = "La cantidad de vehiculos es obligatoria")
    @Min(value = 0, message = "La cantidad de vehiculos no puede ser negativa")
    private Integer cantidadVehiculos;

    @NotNull(message = "La region es obligatoria")
    private Integer regionId;
}
