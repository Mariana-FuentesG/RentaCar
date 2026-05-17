package com.prueba.ms_reservas.service;

import com.prueba.ms_reservas.client.ClienteClient;
import com.prueba.ms_reservas.dto.ClienteDTO;
import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.mapper.ReservaMapper;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.model.Reserva;
import com.prueba.ms_reservas.repository.EstadoReservaRepository;
import com.prueba.ms_reservas.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository reservaRepository;

    @Autowired
    EstadoReservaRepository estadoReservaRepository;

    @Autowired
    ClienteClient clienteClient;

    // GET → LISTAR RESERVAS
    public List<ReservaDTO> obtenerReservas(){
        return reservaRepository.findAll()
                .stream()
                .map(ReservaMapper::toDTO)
                .collect(Collectors.toList());
    }
    // GET → BUSCAR POR ID
    public ReservaDTO obtenerReservaPorId(Integer id){
        Reserva reserva = reservaRepository.findById(id)
                .orElse(null);
        if(reserva == null){
            return null;
        }
        return ReservaMapper.toDTO(reserva);
    }

    // POST → GUARDAR RESERVA
    public ReservaDTO guardarReserva(ReservaDTO dto){
        ClienteDTO cliente = clienteClient.obtenerClientePorId(dto.getClienteId());
        if(cliente == null){return null;}
        Reserva reserva = ReservaMapper.toEntity(dto);
        EstadoReserva estado =
                estadoReservaRepository
                .findById(dto.getEstadoReservaId())
                .orElse(null);
        if(estado == null){return null;}
        reserva.setEstadoReserva(estado);
        Reserva guardada =
                reservaRepository.save(reserva);
        return ReservaMapper.toDTO(guardada);
    }

    // PUT → ACTUALIZAR RESERVA
    public ReservaDTO actualizarReserva(Integer id,
            ReservaDTO dto){
        try {
            Reserva reserva = reservaRepository.findById(id)
                            .orElse(null);
            if(reserva == null){
                return null;
            }
            reserva.setClienteId(dto.getClienteId());
            reserva.setVehiculoId(dto.getVehiculoId());
            reserva.setMontoReserva(dto.getMontoReserva());
            reserva.setCantidadDias(dto.getCantidadDias());
            reserva.setPagada(dto.getPagada());
            reserva.setFechaInicio(dto.getFechaInicio());
            reserva.setFechaTermino(dto.getFechaTermino());
            reserva.setFechaReserva(dto.getFechaReserva());
            reserva.setObservacion(dto.getObservacion());
            EstadoReserva estado = estadoReservaRepository
                            .findById(dto.getEstadoReservaId())
                            .orElse(null);
            if(estado == null){return null;
            }
            reserva.setEstadoReserva(estado);
            Reserva actualizada = reservaRepository.save(reserva);
            return ReservaMapper.toDTO(actualizada);
        }catch (Exception e){
            throw new RuntimeException(
                    "Error al actualizar reserva");
        }
    }
    // JPQL → BUSCAR RESERVAS DESDE FECHA
    public List<ReservaDTO> buscarReservasDesdeFecha(LocalDate fecha){
        return reservaRepository
                .buscarReservasDesdeFecha(fecha)
                .stream()
                .map(ReservaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // DELETE → ELIMINAR RESERVA
    @Transactional
    public boolean eliminarReserva(Integer id){
        try{
            Reserva eliminar = reservaRepository.findById(id)
                            .orElse(null);
            if(eliminar == null){return false;
            }
            reservaRepository.delete(eliminar);return true;
        }catch (Exception e){
            return false;
        }
    }
}
