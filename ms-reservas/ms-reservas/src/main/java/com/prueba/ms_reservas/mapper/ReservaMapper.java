package com.prueba.ms_reservas.mapper;

import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.model.Reserva;

public class ReservaMapper {

    public static ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new  ReservaDTO();

        dto.setId(reserva.getId());
        dto.setClienteId(reserva.getClienteId());
        dto.setVehiculoId(reserva.getVehiculoId());
        dto.setMontoReserva(reserva.getMontoReserva());
        dto.setCantidadDias(reserva.getCantidadDias());
        dto.setPagada(reserva.getPagada());
        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaTermino(reserva.getFechaTermino());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setObservacion(reserva.getObservacion());

        // ESTADO
        if(reserva.getEstadoReserva() != null){
            dto.setEstadoReservaId(
                    reserva.getEstadoReserva().getId());
            dto.setNombreEstado(
                    reserva.getEstadoReserva().getNombre());
        }

        return dto;
    }

    public static Reserva toEntity(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setId(dto.getId());
        reserva.setClienteId(dto.getClienteId());
        reserva.setVehiculoId(dto.getVehiculoId());
        reserva.setMontoReserva(dto.getMontoReserva());
        reserva.setCantidadDias(dto.getCantidadDias());
        reserva.setPagada(dto.getPagada());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaTermino(dto.getFechaTermino());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setObservacion(dto.getObservacion());

        // RELACION MANY TO ONE
        if (dto.getEstadoReservaId() != null) {
            EstadoReserva estadoReserva = new EstadoReserva();
            estadoReserva.setId(dto.getEstadoReservaId());
            reserva.setEstadoReserva(estadoReserva);
        }
        return reserva;
    }
}
