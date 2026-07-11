package com.prueba.ms_sucursales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de salida. Extiende RepresentationModel para soportar enlaces HATEOAS.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponseDTO extends RepresentationModel<SucursalResponseDTO> {

    private Integer id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String ciudad;
    private Boolean activa;
    private Integer cantidadVehiculos;
    private Integer regionId;
    private String regionNombre;
}
