package com.prueba.ms_reportes.mapper;

import com.prueba.ms_reportes.dto.ReporteDTO;
import com.prueba.ms_reportes.model.Reporte;

public class ReporteMapper {
    public static ReporteDTO toDTO(Reporte reporte) {
        return new ReporteDTO(
                reporte.getId(),
                reporte.getTitulo(),
                reporte.getDescripcion(),
                reporte.getTotalReservas(),
                reporte.getTotalIngresos(),
                reporte.getActivo(),
                reporte.getFechaGeneracion()
        );
    }

    public static Reporte toEntity(ReporteDTO dto) {
        return new Reporte(
                dto.getId(),
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getTotalReservas(),
                dto.getTotalIngresos(),
                dto.getActivo(),
                dto.getFechaGeneracion()
        );
    }
}
