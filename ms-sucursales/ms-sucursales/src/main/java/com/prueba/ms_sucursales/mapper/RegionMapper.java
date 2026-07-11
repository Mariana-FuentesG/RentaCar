package com.prueba.ms_sucursales.mapper;

import com.prueba.ms_sucursales.dto.request.RegionRequestDTO;
import com.prueba.ms_sucursales.dto.response.RegionResponseDTO;
import com.prueba.ms_sucursales.model.Region;

public class RegionMapper {

    private RegionMapper() {
    }

    public static RegionResponseDTO toResponseDTO(Region region) {
        RegionResponseDTO dto = new RegionResponseDTO();
        dto.setId(region.getId());
        dto.setNombre(region.getNombre());
        dto.setCodigo(region.getCodigo());
        dto.setActiva(region.getActiva());
        return dto;
    }

    public static Region toEntity(RegionRequestDTO dto) {
        Region region = new Region();
        region.setNombre(dto.getNombre());
        region.setCodigo(dto.getCodigo());
        region.setActiva(dto.getActiva());
        return region;
    }
}
