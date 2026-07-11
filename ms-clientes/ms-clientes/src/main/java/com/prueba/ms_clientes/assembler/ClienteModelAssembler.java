package com.prueba.ms_clientes.assembler;

import com.prueba.ms_clientes.controller.ClienteController;
import com.prueba.ms_clientes.dto.ClienteDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un ClienteDTO en un EntityModel<ClienteDTO> agregando
 * los enlaces HATEOAS (_links) de navegación: self, colección,
 * actualizar y eliminar.
 */
@Component
public class ClienteModelAssembler
        implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {

    @Override
    public @NonNull EntityModel<ClienteDTO> toModel(@NonNull ClienteDTO cliente) {
        return EntityModel.of(cliente,
                // ENLACE SELF → ESTE MISMO CLIENTE
                linkTo(methodOn(ClienteController.class)
                        .obtenerClientePorId(cliente.getId())).withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE CLIENTES
                linkTo(methodOn(ClienteController.class)
                        .listarClientes()).withRel("clientes"),

                // ENLACE → ACTUALIZAR ESTE CLIENTE (PUT)
                linkTo(methodOn(ClienteController.class)
                        .actualizar(cliente.getId(), null)).withRel("actualizar"),

                // ENLACE → ELIMINAR ESTE CLIENTE (DELETE)
                linkTo(methodOn(ClienteController.class)
                        .eliminar(cliente.getId())).withRel("eliminar")
        );
    }
}