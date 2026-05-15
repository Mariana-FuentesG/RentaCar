package com.prueba.ms_clientes.mapper;

import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.model.Direccion;

public class DireccionMapper {
    public static DireccionDTO toDTO(Direccion direccion){
        return new DireccionDTO(
                direccion.getId(),
                direccion.getCalle(),
                direccion.getComuna(),
                direccion.getNumeroCasa(),
                direccion.getCodigoPostal(),
                direccion.getEstado(),
                direccion.getFechaRegistro(),

                //Datos del cliente relacionado

                direccion.getCliente().getId(),
                direccion.getCliente().getNombreCompleto()
        );
    }

    public static Direccion toEntity(DireccionRequestDTO dto){
        return Direccion.builder() //modificar
                .calle(dto.getCalle())
                .comuna(dto.getComuna())
                .numeroCasa(dto.getNumeroCasa())
                .codigoPostal(dto.getCodigoPostal())
                .estado(dto.getEstado())
                .fechaRegistro(dto.getFechaRegistro())
                .build();
    }


}
