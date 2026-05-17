package com.prueba.ms_sucursales.mapper;

import com.prueba.ms_sucursales.dto.SucursalDTO;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;

public class SucursalMapper {
    public static SucursalDTO toDTO(Sucursal sucursal) {
        return new SucursalDTO(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion(),
                sucursal.getTelefono(),
                sucursal.getCiudad(),
                sucursal.getActiva(),
                sucursal.getCantidadVehiculos(),
                sucursal.getRegion().getId()
                );
    }

    public static Sucursal toEntity(SucursalDTO dto) {
        Sucursal sucursal= new Sucursal();
        sucursal.setId(dto.getId());
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());
        sucursal.setCiudad(dto.getCiudad());
        sucursal.setActiva(dto.getActiva());
        sucursal.setCantidadVehiculos(dto.getCantidadVehiculos());

        //RELACION MANY TO ONE
        Region region = new Region();
        region.setId(dto.getRegionId());
        sucursal.setRegion(region);
        return sucursal;
    }
}
