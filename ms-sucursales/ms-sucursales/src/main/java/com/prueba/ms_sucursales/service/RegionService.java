package com.prueba.ms_sucursales.service;

import com.prueba.ms_sucursales.dto.RegionDTO;
import com.prueba.ms_sucursales.mapper.RegionMapper;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegionService {

    @Autowired
    RegionRepository regionRepository;

    // GET → LISTAR REGIONES
    public List<RegionDTO> obtenerRegiones(){
        List<Region> regiones = regionRepository.findAll();
        return regiones.stream()
                .map(RegionMapper::toDTO)
                .toList();
    }

    // GET → BUSCAR REGION POR ID
    public RegionDTO obtenerRegionPorId(Integer id){
        Region region = regionRepository.findById(id)
                        .orElse(null);
        if(region == null){
            return null;
        }
        return RegionMapper.toDTO(region);
    }

    // POST → GUARDAR REGION
    public RegionDTO guardarRegion(RegionDTO dto){
        Region region = RegionMapper.toEntity(dto);
        Region guardada = regionRepository.save(region);
        return RegionMapper.toDTO(guardada);
    }

    // PUT → ACTUALIZAR REGION
    public RegionDTO actualizarRegion(Integer id, RegionDTO dto){
        Region region = regionRepository.findById(id)
                .orElse(null);
        if(region == null){return null;
        }
        region.setNombre(dto.getNombre());
        region.setCodigo(dto.getCodigo());
        region.setActiva(dto.getActiva());

        Region actualizada = regionRepository.save(region);
        return RegionMapper.toDTO(actualizada);
    }

    // DELETE → ELIMINAR REGION
    public boolean eliminarRegion(Integer id){
        if(!regionRepository.existsById(id)){
            return false;
        }
        regionRepository.deleteById(id);
        return true;
    }


}
