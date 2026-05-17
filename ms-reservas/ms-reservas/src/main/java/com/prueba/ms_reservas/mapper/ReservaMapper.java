package com.prueba.ms_reservas.mapper;

import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.model.Reserva;

public class ReservaMapper {
    public static ReservaDTO toDTO(Reserva reserva) {
        return new ReservaDTO(
                reserva.getId(),
                reserva.getClienteId(),
                reserva.getVehiculoId(),
                reserva.getMontoReserva(),
                reserva.getCantidadDias(),
                reserva.getPagada(),
                reserva.getFechaInicio(),
                reserva.getFechaTermino(),
                reserva.getFechaReserva(),
                reserva.getObservacion(),

                //DATOS DEL ESTADO RELACIONADO
                reserva.getEstadoReserva().getId(),
                reserva.getEstadoReserva().getNombre()
        );
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
        EstadoReserva estadoReserva = new EstadoReserva();
        estadoReserva.setId(dto.getEstadoReservaId());
        reserva.setEstadoReserva(estadoReserva);
        return reserva;
    }
}
