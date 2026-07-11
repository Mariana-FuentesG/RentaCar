package com.prueba.ms_empleados.mapper;

import com.prueba.ms_empleados.dto.EmpleadoDTO;
import com.prueba.ms_empleados.model.Empleado;

public class EmpleadoMapper {
    // Dirección: Entidad → DTO
    public static EmpleadoDTO toDTO(Empleado empleado) {
        return new EmpleadoDTO(
                empleado.getId(),
                empleado.getNombreCompleto(),
                empleado.getEmail(),
                empleado.getTelefono(),
                empleado.getCargo(),
                empleado.getSueldo(),
                empleado.getActivo(),
                empleado.getFechaContratacion()
        );
    }
    // Dirección: DTO → Entidad
    public static Empleado toEntity(EmpleadoDTO dto) {
        return new Empleado(
                dto.getId(),
                dto.getNombreCompleto(),
                dto.getEmail(),
                dto.getTelefono(),
                dto.getCargo(),
                dto.getSueldo(),
                dto.getActivo(),
                dto.getFechaContratacion()
        );
    }
}
