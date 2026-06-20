package com.prueba.ms_reservas.assembler;

import com.prueba.ms_reservas.controller.EstadoReservaController;
import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un EstadoReservaDTO en un EntityModel<EstadoReservaDTO> agregando
 * los enlaces HATEOAS (_links) de navegación: self, colección, actualizar y
 * eliminar.
 */
@Component
public class EstadoReservaModelAssembler
        implements RepresentationModelAssembler<EstadoReservaDTO, EntityModel<EstadoReservaDTO>> {

    @Override
    public  @NonNull EntityModel<EstadoReservaDTO> toModel( @NonNull EstadoReservaDTO estado) {
        return EntityModel.of(estado,
                // ENLACE SELF → ESTE MISMO ESTADO
                linkTo(methodOn(EstadoReservaController.class)
                        .obtenerEstadoReservaPorId(estado.getId())).withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE ESTADOS
                linkTo(methodOn(EstadoReservaController.class)
                        .obtenerEstadosReserva()).withRel("estados-reserva"),

                // ENLACE → ACTUALIZAR ESTE ESTADO (PUT)
                linkTo(methodOn(EstadoReservaController.class)
                        .actualizarEstadoReserva(estado.getId(), null)).withRel("actualizar"),

                // ENLACE → ELIMINAR ESTE ESTADO (DELETE)
                linkTo(methodOn(EstadoReservaController.class)
                        .eliminarEstadoReserva(estado.getId())).withRel("eliminar")

        );
    }
}
