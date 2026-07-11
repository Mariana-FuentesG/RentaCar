package com.prueba.ms_sucursales.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El codigo es obligatorio")
    @Size(min = 2, max = 10, message = "El codigo debe tener entre 2 y 10 caracteres")
    private String codigo;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activa;
}
