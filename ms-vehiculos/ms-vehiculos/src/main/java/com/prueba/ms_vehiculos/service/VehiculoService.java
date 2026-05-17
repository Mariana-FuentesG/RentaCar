package com.prueba.ms_vehiculos.service;

import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import com.prueba.ms_vehiculos.mapper.VehiculoMapper;
import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.model.Vehiculo;
import com.prueba.ms_vehiculos.repository.CategoriaRepository;
import com.prueba.ms_vehiculos.repository.VehiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService {

    @Autowired
    VehiculoRepository vehiculoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    // GET → LISTAR TODOS LOS VEHICULOS
    public List<VehiculoDTO> obtenerVehiculos(){
        return vehiculoRepository.findAll()
                .stream()
                .map(VehiculoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // POST → GUARDAR VEHICULO
    public VehiculoDTO guardarVehiculo(VehiculoDTO dto){
        Vehiculo vehiculo =
                VehiculoMapper.toEntity(dto);
        // RELACION MANY TO ONE
        Categoria categoria =
                categoriaRepository.findById(
                                dto.getCategoriaId())
                        .orElse(null);
        vehiculo.setCategoria(categoria);
        Vehiculo guardado =
                vehiculoRepository.save(vehiculo);
        return VehiculoMapper.toDTO(guardado);
    }

    // GET → BUSCAR VEHICULO POR ID
    public VehiculoDTO obtenerVehiculoPorId(Integer id){
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                        .orElse(null);
        if(vehiculo == null){
            return null;
        }
        return VehiculoMapper.toDTO(vehiculo);
    }

    // PUT → ACTUALIZAR VEHICULO
    public VehiculoDTO actualizarVehiculo(Integer id, VehiculoDTO dto){
        try {Vehiculo vehiculo = vehiculoRepository.findById(id)
                            .orElse(null);
            if(vehiculo == null){
                return null;
            }
            vehiculo.setPatente(dto.getPatente());
            vehiculo.setMarca(dto.getMarca());
            vehiculo.setModelo(dto.getModelo());
            vehiculo.setPrecioDiario(dto.getPrecioDiario());
            vehiculo.setAnio(dto.getAnio());
            vehiculo.setDisponible(dto.getDisponible());
            vehiculo.setFechaIngreso(dto.getFechaIngreso());

            // RELACION MANY TO ONE
            Categoria categoria = categoriaRepository.findById(
                                    dto.getCategoriaId())
                            .orElse(null);
            vehiculo.setCategoria(categoria);
            Vehiculo actualizado = vehiculoRepository.save(vehiculo);
            return VehiculoMapper.toDTO(actualizado);
        }catch (Exception e){
            throw new RuntimeException("Error al actualizar vehículo");
        }
    }

    // DELETE → ELIMINAR VEHICULO
    @Transactional
    public boolean eliminarVehiculo(Integer id){
        try {Vehiculo eliminar = vehiculoRepository.findById(id)
                            .orElse(null);
            if(eliminar == null){
                return false;
            }
            vehiculoRepository.delete(eliminar);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    // QUERY METHOD OBLIGATORIO
    public List<VehiculoDTO> obtenerVehiculosDisponiblesPorPrecio(
            Double precio){
        return vehiculoRepository
                .findByDisponibleTrueAndPrecioDiarioLessThan(precio)
                .stream()
                .map(VehiculoMapper::toDTO)
                .collect(Collectors.toList());
    }
}