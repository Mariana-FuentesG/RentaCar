package com.prueba.ms_clientes.assembler;

import com.prueba.ms_clientes.controller.DireccionController;
import com.prueba.ms_clientes.dto.DireccionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un DireccionDTO en un EntityModel<DireccionDTO> agregando
 * los enlaces HATEOAS (_links) de navegación: self, colección,
 * actualizar y eliminar.
 */
@Component
public class DireccionModelAssembler
        implements RepresentationModelAssembler<DireccionDTO, EntityModel<DireccionDTO>> {

    @Override
    public @NonNull EntityModel<DireccionDTO> toModel(@NonNull DireccionDTO direccion) {
        return EntityModel.of(direccion,
                // ENLACE SELF → ESTA MISMA DIRECCION
                linkTo(methodOn(DireccionController.class)
                        .obtenerDireccionPorId(direccion.getId())).withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE DIRECCIONES
                linkTo(methodOn(DireccionController.class)
                        .obtenerDirecciones()).withRel("direcciones"),

                // ENLACE → ACTUALIZAR ESTA DIRECCION (PUT)
                linkTo(methodOn(DireccionController.class)
                        .actualizar(direccion.getId(), null)).withRel("actualizar"),

                // ENLACE → ELIMINAR ESTA DIRECCION (DELETE)
                linkTo(methodOn(DireccionController.class)
                        .eliminar(direccion.getId())).withRel("eliminar")
        );
    }
}