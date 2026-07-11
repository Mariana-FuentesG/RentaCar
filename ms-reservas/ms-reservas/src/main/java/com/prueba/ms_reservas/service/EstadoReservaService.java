package com.prueba.ms_reservas.service;

import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import com.prueba.ms_reservas.mapper.EstadoReservaMapper;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.repository.EstadoReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoReservaService {
    @Autowired
    EstadoReservaRepository estadoReservaRepository;

    // GET → LISTAR TODOS LOS ESTADOS
    public List<EstadoReservaDTO> obtenerEstadosReserva(){
        return estadoReservaRepository.findAll()
                .stream()
                .map(EstadoReservaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // POST → GUARDAR ESTADO
    public EstadoReservaDTO guardarEstadoReserva(EstadoReservaDTO dto){
        EstadoReserva estadoReserva = EstadoReservaMapper.toEntity(dto);
        EstadoReserva guardado = estadoReservaRepository.save(estadoReserva);
        return EstadoReservaMapper.toDTO(guardado);
    }

    // GET → BUSCAR POR ID
    public EstadoReservaDTO obtenerEstadoReservaPorId(Integer id){
        EstadoReserva estado = estadoReservaRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Estado no encontrado"
                        ));
        return EstadoReservaMapper.toDTO(estado);
    }

    // PUT → ACTUALIZAR
    public EstadoReservaDTO actualizarEstadoReserva(
            Integer id,
            EstadoReservaDTO dto){
        try {
            EstadoReserva estadoReserva = estadoReservaRepository.findById(id)
                            .orElse(null);
            if(estadoReserva == null){
                return null;
            }
            estadoReserva.setNombreEstado(dto.getNombreEstado());
            estadoReserva.setPrioridad(dto.getPrioridad());
            estadoReserva.setActivo(dto.getActivo());
            estadoReserva.setFechaCreacion(dto.getFechaCreacion());
            estadoReserva.setDiasLimitePago(dto.getDiasLimitePago());
            EstadoReserva actualizado = estadoReservaRepository.save(estadoReserva);
            return EstadoReservaMapper.toDTO(actualizado);
        }catch (Exception e){
            throw new RuntimeException(
                    "Error al actualizar el estado de la reserva");
        }
    }

    // DELETE
    @Transactional
    public boolean eliminarEstadoReserva(Integer id){
        try {EstadoReserva eliminar = estadoReservaRepository.findById(id)
                            .orElse(null);
            if(eliminar == null){return false;
            }
            estadoReservaRepository.delete(eliminar);return true;
        }catch (Exception e){
            return false;
        }
    }
}