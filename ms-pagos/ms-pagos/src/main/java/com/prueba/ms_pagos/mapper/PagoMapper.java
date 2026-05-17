package com.prueba.ms_pagos.mapper;

import com.prueba.ms_pagos.dto.PagoDTO;
import com.prueba.ms_pagos.model.Pago;

public class PagoMapper {

    // ENTITY → DTO
    public static PagoDTO toDTO(Pago pago){
        return new PagoDTO(

                pago.getId(),
                pago.getReservaId(),
                pago.getMonto(),
                pago.getPagado(),
                pago.getFechaPago(),
                pago.getMetodoPago(),
                pago.getCantCuotas(),

                // DATOS RESERVA
                null,
                null
        );
    }

    // DTO → ENTITY
    public static Pago toEntity(PagoDTO dto){
        Pago pago = new Pago();
        pago.setId(dto.getId());
        pago.setReservaId(dto.getReservaId());
        pago.setMonto(dto.getMonto());
        pago.setPagado(dto.getPagado());
        pago.setFechaPago(dto.getFechaPago());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setCantCuotas(dto.getCantCuotas());

        return pago;
    }
}