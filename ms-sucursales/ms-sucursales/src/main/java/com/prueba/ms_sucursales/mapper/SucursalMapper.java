package com.prueba.ms_sucursales.mapper;

import com.prueba.ms_sucursales.dto.request.SucursalRequestDTO;
import com.prueba.ms_sucursales.dto.response.SucursalResponseDTO;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;

public class SucursalMapper {

    private SucursalMapper() {
    }

    public static SucursalResponseDTO toResponseDTO(Sucursal sucursal) {
        SucursalResponseDTO dto = new SucursalResponseDTO();
        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());
        dto.setTelefono(sucursal.getTelefono());
        dto.setCiudad(sucursal.getCiudad());
        dto.setActiva(sucursal.getActiva());
        dto.setCantidadVehiculos(sucursal.getCantidadVehiculos());
        if (sucursal.getRegion() != null) {
            dto.setRegionId(sucursal.getRegion().getId());
            dto.setRegionNombre(sucursal.getRegion().getNombre());
        }
        return dto;
    }

    public static Sucursal toEntity(SucursalRequestDTO dto) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());
        sucursal.setCiudad(dto.getCiudad());
        sucursal.setActiva(dto.getActiva());
        sucursal.setCantidadVehiculos(dto.getCantidadVehiculos());

        Region region = new Region();
        region.setId(dto.getRegionId());
        sucursal.setRegion(region);
        return sucursal;
    }
}
