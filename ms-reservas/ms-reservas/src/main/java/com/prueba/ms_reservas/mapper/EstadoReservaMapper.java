package com.prueba.ms_reservas.mapper;

import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import com.prueba.ms_reservas.model.EstadoReserva;

public class EstadoReservaMapper {
    // ENTITY → DTO
    public static EstadoReservaDTO toDTO(EstadoReserva estadoReserva){
        return new EstadoReservaDTO(
                estadoReserva.getId(),
                estadoReserva.getNombre(),
                estadoReserva.getPrioridad(),
                estadoReserva.getActivo(),
                estadoReserva.getFechaCreacion(),
                estadoReserva.getDiasLimitePago()
        );
    }

    // DTO → ENTITY
    public static EstadoReserva toEntity(EstadoReservaDTO dto){
        return new EstadoReserva(
                dto.getId(),
                dto.getNombre(),
                dto.getPrioridad(),
                dto.getActivo(),
                dto.getFechaCreacion(),
                dto.getDiasLimitePago(),
                null
        );
    }
}
