package com.prueba.ms_vehiculos.assembler;

import com.prueba.ms_vehiculos.controller.VehiculoController;
import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Convierte un VehiculoDTO en un EntityModel<VehiculoDTO> agregando
 * los enlaces HATEOAS (_links) de navegación: self, colección,
 * actualizar y eliminar.
 */
@Component
public class VehiculoModelAssembler
        implements RepresentationModelAssembler<VehiculoDTO, EntityModel<VehiculoDTO>> {

    @Override
    public @NonNull EntityModel<VehiculoDTO> toModel(@NonNull VehiculoDTO vehiculo) {
        return EntityModel.of(vehiculo,
                // ENLACE SELF → ESTE MISMO VEHÍCULO
                linkTo(methodOn(VehiculoController.class)
                        .obtenerVehiculoPorId(vehiculo.getId())).withSelfRel(),

                // ENLACE → COLECCIÓN COMPLETA DE VEHÍCULO
                linkTo(methodOn(VehiculoController.class)
                        .obtenerVehiculos()).withRel("vehiculos"),

                // ENLACE → ACTUALIZAR ESTE VEHÍCULO (PUT)
                linkTo(methodOn(VehiculoController.class)
                        .actualizarVehiculo(vehiculo.getId(), null)).withRel("actualizar"),

                // ENLACE → ELIMINAR ESTE VEHÍCULO (DELETE)
                linkTo(methodOn(VehiculoController.class)
                        .eliminarVehiculo(vehiculo.getId())).withRel("eliminar")
        );
    }
}