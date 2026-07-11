package com.prueba.ms_reportes.mapper;

import com.prueba.ms_reportes.dto.request.ReporteRequestDTO;
import com.prueba.ms_reportes.dto.response.ReporteResponseDTO;
import com.prueba.ms_reportes.model.Reporte;

public class ReporteMapper {

    private ReporteMapper() {
    }

    public static ReporteResponseDTO toResponseDTO(Reporte reporte) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setId(reporte.getId());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setTotalReservas(reporte.getTotalReservas());
        dto.setTotalIngresos(reporte.getTotalIngresos());
        dto.setActivo(reporte.getActivo());
        dto.setFechaGeneracion(reporte.getFechaGeneracion());
        return dto;
    }

    public static Reporte toEntity(ReporteRequestDTO dto) {
        Reporte reporte = new Reporte();
        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setTotalReservas(dto.getTotalReservas());
        reporte.setTotalIngresos(dto.getTotalIngresos());
        reporte.setActivo(dto.getActivo());
        reporte.setFechaGeneracion(dto.getFechaGeneracion());
        return reporte;
    }
}
