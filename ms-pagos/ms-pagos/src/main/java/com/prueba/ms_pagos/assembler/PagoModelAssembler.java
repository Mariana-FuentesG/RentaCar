package com.prueba.ms_pagos.assembler;

import com.prueba.ms_pagos.controller.PagoController;
import com.prueba.ms_pagos.dto.PagoDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoDTO, EntityModel<PagoDTO>> {

    private static final String RESERVAS_BASE_URL = "http://localhost:8080/api/v1/reservas/";

    @Override
    public @NonNull EntityModel<PagoDTO> toModel(@NonNull PagoDTO pago) {

        EntityModel<PagoDTO> model = EntityModel.of(pago,
                linkTo(methodOn(PagoController.class)
                        .obtenerPagoPorId(pago.getId())).withSelfRel(),

                linkTo(methodOn(PagoController.class)
                        .obtenerPagos()).withRel("pagos"),

                linkTo(methodOn(PagoController.class)
                        .actualizarPago(pago.getId(), null)).withRel("actualizar"),

                linkTo(methodOn(PagoController.class)
                        .eliminarPago(pago.getId())).withRel("eliminar")
        );

        if (pago.getReservaId() != null) {
            model.add(Link.of(RESERVAS_BASE_URL + pago.getReservaId(), "reserva"));
        }

        return model;
    }

    @Override
    public @NonNull CollectionModel<EntityModel<PagoDTO>> toCollectionModel(
            @NonNull Iterable<? extends PagoDTO> pagos) {

        CollectionModel<EntityModel<PagoDTO>> collection =
                RepresentationModelAssembler.super.toCollectionModel(pagos);

        collection.add(linkTo(methodOn(PagoController.class)
                .obtenerPagos()).withSelfRel());

        return collection;
    }
}