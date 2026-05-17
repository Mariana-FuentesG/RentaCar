package com.prueba.ms_vehiculos.mapper;

import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.model.Vehiculo;

public class VehiculoMapper {

    // ENTITY → DTO
    public static VehiculoDTO toDTO(Vehiculo vehiculo){
        return new VehiculoDTO(
                vehiculo.getId(),
                vehiculo.getPatente(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getPrecioDiario(),
                vehiculo.getAnio(),
                vehiculo.getDisponible(),
                vehiculo.getFechaIngreso(),

                // RELACION
                vehiculo.getCategoria().getId(),
                vehiculo.getCategoria().getNombre()
        );
    }

    // DTO → ENTITY
    public static Vehiculo toEntity(VehiculoDTO dto){

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(dto.getId());
        vehiculo.setPatente(dto.getPatente());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setPrecioDiario(dto.getPrecioDiario());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setDisponible(dto.getDisponible());
        vehiculo.setFechaIngreso(dto.getFechaIngreso());

        // RELACION
        Categoria categoria = new Categoria();
        categoria.setId(dto.getCategoriaId());
        vehiculo.setCategoria(categoria);
        return vehiculo;
    }
}