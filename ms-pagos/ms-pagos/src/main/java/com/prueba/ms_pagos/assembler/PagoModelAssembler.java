package com.prueba.ms_pagos.assembler;

import com.prueba.ms_pagos.controller.PagoController;
import com.prueba.ms_pagos.dto.PagoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un PagoDTO en un EntityModel<PagoDTO> agregando los enlaces
 * HATEOAS (_links) de navegación: self, colección, actualizar, eliminar y
 * la reserva relacionada (recurso ubicado en ms-reservas vía API Gateway).
 */
@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoDTO, EntityModel<PagoDTO>> {

    // URL BASE DEL API GATEWAY → PUNTO DE ENTRADA ÚNICO PARA ACCEDER
    // A RECURSOS DE OTROS MICROSERVICIOS
    private static final String RESERVAS_BASE_URL = "http://localhost:8080/api/v1/reservas/";

    @Override
    public @NonNull EntityModel<PagoDTO> toModel(@NonNull PagoDTO pago) {

        EntityModel<PagoDTO> model = EntityModel.of(pago,
                // ENLACE SELF → ESTE MISMO PAGO
                linkTo(methodOn(PagoController.class)
                        .obtenerPagoPorId(pago.getId())).withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE PAGOS
                linkTo(methodOn(PagoController.class)
                        .obtenerPagos()).withRel("pagos"),

                // ENLACE → ACTUALIZAR ESTE PAGO (PUT)
                linkTo(methodOn(PagoController.class)
                        .actualizarPago(pago.getId(), null)).withRel("actualizar"),

                // ENLACE → ELIMINAR ESTE PAGO (DELETE)
                linkTo(methodOn(PagoController.class)
                        .eliminarPago(pago.getId())).withRel("eliminar")
        );

        // ENLACE → RESERVA RELACIONADA (microservicio externo ms-reservas)
        if (pago.getReservaId() != null) {
            model.add(Link.of(RESERVAS_BASE_URL + pago.getReservaId(), "reserva"));
        }

        return model;
    }
}