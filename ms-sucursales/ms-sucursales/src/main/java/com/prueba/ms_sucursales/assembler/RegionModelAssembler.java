package com.prueba.ms_sucursales.assembler;

import com.prueba.ms_sucursales.controller.RegionController;
import com.prueba.ms_sucursales.dto.response.RegionResponseDTO;
import com.prueba.ms_sucursales.mapper.RegionMapper;
import com.prueba.ms_sucursales.model.Region;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<Region, RegionResponseDTO> {

    @Override
    public RegionResponseDTO toModel(Region region) {
        RegionResponseDTO dto = RegionMapper.toResponseDTO(region);

        dto.add(linkTo(methodOn(RegionController.class).obtenerRegionPorId(region.getId())).withSelfRel());
        dto.add(linkTo(methodOn(RegionController.class).obtenerRegiones()).withRel("regiones"));

        return dto;
    }
}
