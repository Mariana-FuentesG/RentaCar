package com.prueba.ms_sucursales.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class RegionResponseDTO extends RepresentationModel<RegionResponseDTO> {

    private Integer id;
    private String nombre;
    private String codigo;
    private Boolean activa;
}
