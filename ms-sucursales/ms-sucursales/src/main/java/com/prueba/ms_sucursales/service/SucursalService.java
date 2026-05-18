package com.prueba.ms_sucursales.service;

import com.prueba.ms_sucursales.dto.SucursalDTO;
import com.prueba.ms_sucursales.mapper.SucursalMapper;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.repository.RegionRepository;
import com.prueba.ms_sucursales.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class SucursalService {
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private RegionRepository regionRepository;

    // GET → LISTAR SUCURSALES
    public List<SucursalDTO> obtenerSucursales(){
        return sucursalRepository.findAll()
                .stream()
                .map(SucursalMapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET → BUSCAR SUCURSAL POR ID
    public SucursalDTO obtenerSucursalPorId(Integer id){
        Sucursal sucursal = sucursalRepository.findById(id)
                        .orElse(null);
        if(sucursal == null){
            return null;
        }
        return SucursalMapper.toDTO(sucursal);
    }

    // POST → GUARDAR SUCURSAL
    public SucursalDTO guardarSucursal(SucursalDTO dto){
        Region region = regionRepository.findById(dto.getRegionId())
                        .orElse(null);

        if(region == null){
            return null;
        }
        Sucursal sucursal = SucursalMapper.toEntity(dto);

        sucursal.setRegion(region);
        Sucursal guardada = sucursalRepository.save(sucursal);
        return SucursalMapper.toDTO(guardada);
    }

    // PUT → ACTUALIZAR SUCURSAL
    public SucursalDTO actualizarSucursal(Integer id, SucursalDTO dto){
        try {Sucursal sucursal = sucursalRepository.findById(id)
                    .orElse(null);
            if (sucursal == null) {
                return null;
            }
            Region region = regionRepository.findById(dto.getRegionId())
                    .orElse(null);
            if (region == null) {return null;
            }
            // ACTUALIZAR CAMPOS INDIVIDUALMENTE
            sucursal.setNombre(dto.getNombre());
            sucursal.setDireccion(dto.getDireccion());
            sucursal.setTelefono(dto.getTelefono());
            sucursal.setCiudad(dto.getCiudad());
            sucursal.setActiva(dto.getActiva());
            sucursal.setCantidadVehiculos(dto.getCantidadVehiculos());

            sucursal.setRegion(region);
            Sucursal actualizada = sucursalRepository.save(sucursal);

            return SucursalMapper.toDTO(actualizada);
        }catch(Exception e){
            return null;
        }
    }

    // DELETE → ELIMINAR SUCURSAL
    public boolean eliminarSucursal(Integer id){
        if(!sucursalRepository.existsById(id)){
            return false;
        }
        sucursalRepository.deleteById(id);
        return true;
    }

    //QUERY NATIVE → LISTAR SUCURSALES OPERATIVAS
    public List<SucursalDTO> obtenerSucursalesOperativas(){
        return sucursalRepository.obtenerSucursalesOperativas()
                .stream()
                .map(SucursalMapper::toDTO)
                .collect(Collectors.toList());
    }
}
