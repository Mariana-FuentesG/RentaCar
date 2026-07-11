package com.prueba.ms_empleados.service;

import com.prueba.ms_empleados.dto.EmpleadoDTO;
import com.prueba.ms_empleados.mapper.EmpleadoMapper;
import com.prueba.ms_empleados.model.Empleado;
import com.prueba.ms_empleados.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    // GET → LISTAR TODOS LOS EMPLEADOS
    public List<EmpleadoDTO> obtenerEmpleados(){
        return empleadoRepository.findAll()
                .stream()
                .map(EmpleadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET → OBTENER EMPLEADO POR ID
    public EmpleadoDTO obtenerEmpleadoPorId(Integer id){
        Empleado empleado = empleadoRepository.findById(id)
                        .orElse(null);
        if(empleado == null){return null;
        }
        return EmpleadoMapper.toDTO(empleado);
    }

    // POST → GUARDAR EMPLEADO
    public EmpleadoDTO guardarEmpleado(EmpleadoDTO dto){
        try{
            Empleado empleado = EmpleadoMapper.toEntity(dto);
            Empleado guardado = empleadoRepository.save(empleado);
            return EmpleadoMapper.toDTO(guardado);
        }catch (Exception e){
            return null;
        }
    }

    // PUT → ACTUALIZAR EMPLEADO
    public EmpleadoDTO actualizarEmpleado(Integer id, EmpleadoDTO dto){
        try{
            Empleado empleado = empleadoRepository.findById(id)
                            .orElse(null);
            if(empleado == null){return null;
            }
            // ACTUALIZAR CAMPOS INDIVIDUALMENTE
            empleado.setNombreCompleto(dto.getNombreCompleto());
            empleado.setEmail(dto.getEmail());
            empleado.setTelefono(dto.getTelefono());
            empleado.setCargo(dto.getCargo());
            empleado.setSueldo(dto.getSueldo());
            empleado.setActivo(dto.getActivo());
            empleado.setFechaContratacion(dto.getFechaContratacion());
            Empleado actualizado = empleadoRepository.save(empleado);
            return EmpleadoMapper.toDTO(actualizado);
        }catch (Exception e){
            return null;
        }
    }

    // DELETE → ELIMINAR EMPLEADO
    public boolean eliminarEmpleado(Integer id){
        try{
            if(!empleadoRepository.existsById(id)){
                return false;
            }
            empleadoRepository.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // QUERY NATIVE
    // LISTAR EMPLEADOS ACTIVOS POR AÑO
    public List<EmpleadoDTO> obtenerEmpleadosActivosPorAnio(Integer anio){
        return empleadoRepository
                .obtenerEmpleadosActivosPorAnio(anio)
                .stream()
                .map(EmpleadoMapper::toDTO)
                .collect(Collectors.toList());
    }
}