package com.prueba.ms_sucursales.assembler;

import com.prueba.ms_sucursales.controller.SucursalController;
import com.prueba.ms_sucursales.dto.response.SucursalResponseDTO;
import com.prueba.ms_sucursales.mapper.SucursalMapper;
import com.prueba.ms_sucursales.model.Sucursal;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Centraliza la construcción de enlaces HATEOAS para Sucursal.
 * Los Controllers nunca arman links manualmente: siempre pasan por aquí.
 */
@Component
public class SucursalModelAssembler implements RepresentationModelAssembler<Sucursal, SucursalResponseDTO> {

    @Override
    public SucursalResponseDTO toModel(Sucursal sucursal) {
        SucursalResponseDTO dto = SucursalMapper.toResponseDTO(sucursal);

        dto.add(linkTo(methodOn(SucursalController.class).obtenerSucursalPorId(sucursal.getId())).withSelfRel());
        dto.add(linkTo(methodOn(SucursalController.class).obtenerSucursales()).withRel("sucursales"));
        dto.add(linkTo(methodOn(SucursalController.class).obtenerSucursalesOperativas()).withRel("operativas"));

        return dto;
    }
}
