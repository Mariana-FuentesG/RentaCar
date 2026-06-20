package com.prueba.ms_reservas.service;

import com.prueba.ms_reservas.client.ClienteClient;
import com.prueba.ms_reservas.client.VehiculoClient;
import com.prueba.ms_reservas.dto.ClienteDTO;
import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.dto.VehiculoDTO;
import com.prueba.ms_reservas.mapper.ReservaMapper;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.model.Reserva;
import com.prueba.ms_reservas.repository.EstadoReservaRepository;
import com.prueba.ms_reservas.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservaService {

    @Autowired
    ReservaRepository reservaRepository;

    @Autowired
    EstadoReservaRepository estadoReservaRepository;

    @Autowired
    ClienteClient clienteClient;

    @Autowired
    VehiculoClient vehiculoClient;

    // GET → LISTAR RESERVAS
    public List<ReservaDTO> obtenerReservas() {
        return reservaRepository.findAll()
                .stream()
                .map(this::convertirConDetalles)
                .collect(Collectors.toList());
    }
    // GET → BUSCAR RESERVA POR ID
    public ReservaDTO obtenerReservaPorId(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                        .orElse(null);
        if (reserva == null) {
            return null;
        }
        return convertirConDetalles(reserva);
    }

    // POST → GUARDAR RESERVA
    public ReservaDTO guardarReserva(ReservaDTO dto) {
        // VALIDAR CLIENTE
        try {
            ClienteDTO cliente = clienteClient.obtenerClientePorId(
                    dto.getClienteId());
            if (cliente == null) {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Servicio clientes no disponible"
            );
        }
        // VALIDAR VEHICULO
        try {
            VehiculoDTO vehiculo =
                    vehiculoClient.obtenerVehiculoPorId(
                            dto.getVehiculoId());
            if (vehiculo == null) {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Servicio vehículos no disponible"
            );
        }
        //MAPEAR
        Reserva reserva = ReservaMapper.toEntity(dto);
        //VALIDAR ESTADO
        EstadoReserva estado =
                estadoReservaRepository
                        .findById(dto.getEstadoReservaId())
                        .orElse(null);
        if (estado == null) {
            return null;
        }
        reserva.setEstadoReserva(
                estado);
        try {
            Reserva guardada =
                    reservaRepository
                            .save(reserva);
            return convertirConDetalles(guardada);

        } catch (Exception e) {
            throw new RuntimeException(
                    "❌ Error al guardar reserva: "
            );
        }
    }

    // PUT → ACTUALIZAR RESERVA
    public ReservaDTO actualizarReserva(Integer id, ReservaDTO dto) {

        try {
            Reserva reserva = reservaRepository.findById(id)
                            .orElse(null);
            if (reserva == null) {
                return null;
            }
            // VALIDAR CLIENTE
            ClienteDTO cliente = clienteClient.obtenerClientePorId(dto.getClienteId());
            if (cliente == null) {
                return null;
            }
            // VALIDAR VEHICULO
            VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculoPorId(
                            dto.getVehiculoId());
            if (vehiculo == null) {
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
            if (estado == null) {
                return null;
            }
            reserva.setEstadoReserva(estado);
            Reserva actualizada = reservaRepository.save(reserva);

            return convertirConDetalles(actualizada);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error al actualizar reserva");
        }
    }

    // JPQL → BUSCAR RESERVAS DESDE FECHA
    public List<ReservaDTO>
    buscarReservasDesdeFecha(LocalDate fecha) {
        return reservaRepository
                .buscarReservasDesdeFecha(fecha)
                .stream()
                .map(this::convertirConDetalles)
                .collect(Collectors.toList());
    }

    // DELETE → ELIMINAR RESERVA
    @Transactional
    public boolean eliminarReserva(Integer id) {
        try {
            Reserva eliminar = reservaRepository.findById(id)
                            .orElse(null);
            if (eliminar == null) {
                return false;
            }
            reservaRepository.delete(eliminar);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

   /// FEIGNCLIENT → COMPLETAR DATOS
   private ReservaDTO convertirConDetalles(Reserva reserva) {
       ReservaDTO dto = ReservaMapper.toDTO(reserva);

       // CLIENTE
       try {
           ClienteDTO cliente = clienteClient.obtenerClientePorId(
                   reserva.getClienteId());
           log.info(">>> Cliente recibido: {}", cliente);
           log.info(">>> NombreCompleto: {}", cliente != null ? cliente.getNombreCompleto() : "NULL");
           if (cliente != null) {
               dto.setNombreCliente(
                       cliente.getNombreCompleto());
           }
       } catch (Exception e) {
           log.error("❌ Error cliente: {}", e.getMessage());
           dto.setNombreCliente(
                   "❌ Servicio clientes no disponible");
       }

       // VEHICULO
       try {
           VehiculoDTO vehiculo =
                   vehiculoClient.obtenerVehiculoPorId(
                           reserva.getVehiculoId());
           if (vehiculo != null) {
               dto.setMarcaVehiculo(vehiculo.getMarca());
               dto.setModeloVehiculo(vehiculo.getModelo());
           }
       } catch (Exception e) {
           dto.setMarcaVehiculo(
                   "❌ Marca vehículos no disponible");
           dto.setModeloVehiculo(
                   "❌ Modelo vehículos no disponible");
       }
       return dto;
   }
}