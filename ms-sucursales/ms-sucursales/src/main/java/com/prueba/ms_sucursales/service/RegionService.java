package com.prueba.ms_sucursales.service;

import com.prueba.ms_sucursales.dto.request.RegionRequestDTO;
import com.prueba.ms_sucursales.exception.ResourceNotFoundException;
import com.prueba.ms_sucursales.mapper.RegionMapper;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    private static final Logger log = LoggerFactory.getLogger(RegionService.class);

    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public List<Region> obtenerRegiones() {
        log.info("Listando todas las regiones");
        return regionRepository.findAll();
    }

    public Region obtenerRegionPorId(Integer id) {
        log.info("Buscando region con id {}", id);
        return regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró región con id {}", id);
                    return new ResourceNotFoundException("Región no encontrada con id: " + id);
                });
    }

    public Region guardarRegion(RegionRequestDTO dto) {
        log.info("Creando nueva region: {}", dto.getNombre());
        Region region = RegionMapper.toEntity(dto);
        Region guardada = regionRepository.save(region);
        log.info("Region creada con id {}", guardada.getId());
        return guardada;
    }

    public Region actualizarRegion(Integer id, RegionRequestDTO dto) {
        log.info("Actualizando region con id {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Intento de actualizar región inexistente con id {}", id);
                    return new ResourceNotFoundException("Región no encontrada con id: " + id);
                });

        region.setNombre(dto.getNombre());
        region.setCodigo(dto.getCodigo());
        region.setActiva(dto.getActiva());

        Region actualizada = regionRepository.save(region);
        log.info("Region con id {} actualizada correctamente", id);
        return actualizada;
    }

    public void eliminarRegion(Integer id) {
        log.info("Eliminando region con id {}", id);
        if (!regionRepository.existsById(id)) {
            log.warn("Intento de eliminar region inexistente con id {}", id);
            throw new ResourceNotFoundException("Región no encontrada con id: " + id);
        }
        regionRepository.deleteById(id);
        log.info("Region con id {} eliminada", id);
    }
}
