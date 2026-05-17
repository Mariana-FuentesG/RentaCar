package com.prueba.ms_vehiculos.mapper;

import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import com.prueba.ms_vehiculos.model.Categoria;

public class CategoriaMapper {

    // ENTITY → DTO
    public static CategoriaDTO toDTO(Categoria categoria){
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getCapacidadPasajeros(),
                categoria.getActiva(),
                categoria.getFechaCreacion(),
                categoria.getPrecioBase()
        );
    }

    // DTO → ENTITY
    public static Categoria toEntity(CategoriaDTO dto){
        Categoria categoria = new Categoria();
        categoria.setId(dto.getId());
        categoria.setNombre(dto.getNombre());
        categoria.setCapacidadPasajeros(dto.getCapacidadPasajeros());
        categoria.setActiva(dto.getActiva());
        categoria.setFechaCreacion(dto.getFechaCreacion());
        categoria.setPrecioBase(dto.getPrecioBase());
        return categoria;
    }
}