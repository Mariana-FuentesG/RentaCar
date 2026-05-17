package com.prueba.ms_sucursales.mapper;

import com.prueba.ms_sucursales.dto.RegionDTO;
import com.prueba.ms_sucursales.model.Region;

public class RegionMapper {
    public static RegionDTO toDTO(Region region){
        return new RegionDTO(
                region.getId(),
                region.getNombre(),
                region.getCodigo(),
                region.getActiva()
        );
    }

    public static Region toEntity(RegionDTO dto){
        Region region = new Region();
        region.setId(dto.getId());
        region.setNombre(dto.getNombre());
        region.setCodigo(dto.getCodigo());
        region.setActiva(dto.getActiva());
        return region;
    }
}
