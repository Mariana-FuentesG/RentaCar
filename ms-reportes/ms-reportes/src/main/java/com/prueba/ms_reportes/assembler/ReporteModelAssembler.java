package com.prueba.ms_reportes.assembler;

import com.prueba.ms_reportes.controller.ReporteController;
import com.prueba.ms_reportes.dto.response.ReporteResponseDTO;
import com.prueba.ms_reportes.mapper.ReporteMapper;
import com.prueba.ms_reportes.model.Reporte;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Centraliza la construcción de enlaces HATEOAS para Reporte.
 */
@Component
public class ReporteModelAssembler implements RepresentationModelAssembler<Reporte, ReporteResponseDTO> {

    @Override
    public ReporteResponseDTO toModel(Reporte reporte) {
        ReporteResponseDTO dto = ReporteMapper.toResponseDTO(reporte);

        dto.add(linkTo(methodOn(ReporteController.class).obtenerReportePorId(reporte.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ReporteController.class).obtenerReportes()).withRel("reportes"));
        dto.add(linkTo(methodOn(ReporteController.class).obtenerReportesActivos()).withRel("activos"));

        return dto;
    }
}
