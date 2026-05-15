package com.prueba.ms_clientes.service;

import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.mapper.DireccionMapper;
import com.prueba.ms_clientes.model.Direccion;
import com.prueba.ms_clientes.repository.DireccionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DireccionService {

    @Autowired
    DireccionRepository direccionRepository;

    // GET → LISTAR TODAS LAS DIRECCIONES
    public List<DireccionDTO> obtenerDirecciones(){
        return direccionRepository.findAll()
                .stream()
                .map(DireccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET → OBTENER DIRECCION POR ID
    public DireccionDTO obtenerDireccionPorId(Integer id){
        Direccion direccion = direccionRepository.findById(id)
                .orElse(null);
        if(direccion == null){
            return null;
        }
        return DireccionMapper.toDTO(direccion);
    }

    // POST → GUARDAR DIRECCION
    public DireccionDTO guardarDireccion(DireccionDTO dto){
        Direccion direccion = DireccionMapper.toEntity(dto);
        Direccion guardada = direccionRepository.save(direccion);
        return DireccionMapper.toDTO(guardada);
    }

    // PUT → ACTUALIZAR DIRECCION

    public DireccionDTO actualizarDireccion(
            Integer id,
            DireccionDTO dto){

        Direccion direccion = direccionRepository.findById(id)
                .orElse(null);
        if(direccion == null){
            return null;
        }
        direccion.setCalle(dto.getCalle());
        direccion.setNumeroCasa(dto.getNumeroCasa());
        direccion.setCiudad(dto.getCiudad());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setEstado(dto.getEstado());
        direccion.setFechaRegistro(dto.getFechaRegistro());

        Direccion actualizada =
                direccionRepository.save(direccion);
        return DireccionMapper.toDTO(actualizada);
    }

    // GET → BUSCAR DIRECCIONES POR CIUDAD
    public List<DireccionDTO> buscarPorCiudad(String ciudad){
        return direccionRepository
                .findByCiudadContainingIgnoreCase(ciudad)
                .stream()
                .map(DireccionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // DELETE → ELIMINAR DIRECCION POR ID
    @Transactional
    public boolean eliminarDireccion(Integer id){
        Direccion direccion = direccionRepository.findById(id)
                .orElse(null);
        if(direccion == null){
            return false;
        }
        direccionRepository.delete(direccion);
        return true;
    }
}
