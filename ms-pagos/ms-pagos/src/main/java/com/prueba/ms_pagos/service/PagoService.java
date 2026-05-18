package com.prueba.ms_pagos.service;

import com.prueba.ms_pagos.client.ReservaClient;
import com.prueba.ms_pagos.dto.PagoDTO;
import com.prueba.ms_pagos.dto.ReservaDTO;
import com.prueba.ms_pagos.mapper.PagoMapper;
import com.prueba.ms_pagos.model.Pago;
import com.prueba.ms_pagos.repository.PagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    @Autowired
    PagoRepository pagoRepository;

    @Autowired
    ReservaClient reservaClient;

    // GET → LISTAR PAGOS
    public List<PagoDTO> obtenerPagos(){
        return pagoRepository.findAll()
                .stream()
                .map(PagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET → BUSCAR PAGO POR ID
    public PagoDTO obtenerPagoPorId(Integer id){
        Pago pago = pagoRepository.findById(id)
                .orElse(null);
        if(pago == null){return null;}
        PagoDTO dto = PagoMapper.toDTO(pago);
        // FEIGN → OBTENER RESERVA
        ReservaDTO reserva = reservaClient
                        .obtenerReservaPorId(pago.getReservaId());
        if(reserva != null){
            dto.setMontoReserva(reserva.getMontoReserva());
            dto.setReservaPagada(reserva.getPagada());
        }
        return dto;
    }

    // POST → GUARDAR PAGO
    public PagoDTO guardarPago(PagoDTO dto){
        // VALIDAR RESERVA
        ReservaDTO reserva = reservaClient.obtenerReservaPorId(
                                dto.getReservaId());
        if(reserva == null){return null;}
        Pago pago = PagoMapper.toEntity(dto);
        Pago guardado = pagoRepository.save(pago);
        return PagoMapper.toDTO(guardado);
    }

    // PUT → ACTUALIZAR PAGO
    public PagoDTO actualizarPago(Integer id, PagoDTO dto){
        try{Pago pago = pagoRepository
                    .findById(id)
                    .orElse(null);
            if(pago == null){return null;
            }
            // VALIDAR RESERVA CON FEIGN
            ReservaDTO reserva = reservaClient.obtenerReservaPorId(
                            dto.getReservaId());
            if(reserva == null){
                return null;
            }
            pago.setReservaId(dto.getReservaId());
            pago.setMonto(dto.getMonto());
            pago.setPagado(dto.getPagado());
            pago.setFechaPago(dto.getFechaPago());
            pago.setMetodoPago(dto.getMetodoPago());
            pago.setCantCuotas(dto.getCantCuotas());

            Pago actualizado = pagoRepository.save(pago);
            return PagoMapper.toDTO(actualizado);
        }catch
        (Exception e){
            throw new RuntimeException(
                    "❌ Error al actualizar pago");
        }
    }

    // DELETE → ELIMINAR PAGO
    @Transactional
    public boolean eliminarPago(Integer id){
        try{Pago pago = pagoRepository
                    .findById(id)
                    .orElse(null);
            if(pago == null){return false;}
            pagoRepository.delete(pago);
            return true;
        }catch (Exception e){return false;}
    }

    // JPQL → BUSCAR PAGOS POR RANGO
    public List<PagoDTO> buscarPagosPorMonto(Double minimo, Double maximo){
        return pagoRepository
                .buscarPagosPorMonto(
                        minimo,
                        maximo)
                .stream()
                .map(PagoMapper::toDTO)
                .collect(Collectors.toList());
    }
}