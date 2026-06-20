package com.prueba.ms_reservas.assembler;

import com.prueba.ms_reservas.controller.EstadoReservaController;
import com.prueba.ms_reservas.controller.ReservaController;
import com.prueba.ms_reservas.dto.ReservaDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un ReservaDTO en un EntityModel<ReservaDTO> agregando los
 * enlaces HATEOAS (_links) de navegación: self, colección, actualizar,
 * eliminar y el estado de reserva relacionado.
 */
@Component
public class ReservaModelAssembler
        implements RepresentationModelAssembler<ReservaDTO, EntityModel<ReservaDTO>> {

    @Override
    public  @NonNull EntityModel<ReservaDTO> toModel( @NonNull ReservaDTO reserva) {

        EntityModel<ReservaDTO> model = EntityModel.of(reserva,
                // ENLACE SELF → ESTA MISMA RESERVA
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservaPorId(reserva.getId()))
                        .withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE RESERVAS
                linkTo(methodOn(ReservaController.class)
                        .obtenerReservas())
                        .withRel("collection"),

                // ENLACE → ACTUALIZAR ESTA RESERVA (PUT)
                linkTo(methodOn(ReservaController.class)
                        .actualizarReserva(reserva.getId(),null))
                        .withRel("updateReserva"),

                // ENLACE → ELIMINAR ESTA RESERVA (DELETE)
                linkTo(methodOn(ReservaController.class)
                        .eliminarReserva(reserva.getId()))
                        .withRel("delete")
        );

        // ENLACE → ESTADO DE RESERVA RELACIONADO
        if (reserva.getEstadoReservaId() != null) {
            model.add(linkTo(methodOn(EstadoReservaController.class)
                    .obtenerEstadoReservaPorId(reserva.getEstadoReservaId()))
                    .withRel("estado-reserva"));
        }
        return model;
    }
}
