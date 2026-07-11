package com.prueba.ms_sucursales.service;

import com.prueba.ms_sucursales.dto.request.SucursalRequestDTO;
import com.prueba.ms_sucursales.exception.ResourceNotFoundException;
import com.prueba.ms_sucursales.mapper.SucursalMapper;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.repository.RegionRepository;
import com.prueba.ms_sucursales.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * La lógica de negocio vive aquí. El Service trabaja con entidades;
 * la conversión a DTO + enlaces HATEOAS ocurre en el assembler, no aquí.
 */
@Service
public class SucursalService {

    private static final Logger log = LoggerFactory.getLogger(SucursalService.class);

    private final SucursalRepository sucursalRepository;
    private final RegionRepository regionRepository;

    public SucursalService(SucursalRepository sucursalRepository, RegionRepository regionRepository) {
        this.sucursalRepository = sucursalRepository;
        this.regionRepository = regionRepository;
    }
    // → LISTAR SUCURSALES
    public List<Sucursal> obtenerSucursales() {
        log.info("Listando todas las sucursales");
        return sucursalRepository.findAll();
    }
    // → BUSCAR SUCURSAL POR ID
    public Sucursal obtenerSucursalPorId(Integer id) {
        log.info("Buscando sucursal con id {}", id);
        return sucursalRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Sucursal con id {} no encontrada", id);
                    return new ResourceNotFoundException("Sucursal no encontrada con id: " + id);
                });
    }
    // → GUARDAR SUCURSAL
    public Sucursal guardarSucursal(SucursalRequestDTO dto) {
        log.info("Creando nueva sucursal: {}", dto.getNombre());
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> {
                    log.warn("Region con id {} no encontrada al crear sucursal", dto.getRegionId());
                    return new ResourceNotFoundException("Región no encontrada con id: " + dto.getRegionId());
                });

        Sucursal sucursal = SucursalMapper.toEntity(dto);
        sucursal.setRegion(region);
        Sucursal guardada = sucursalRepository.save(sucursal);
        log.info("Sucursal creada con id {}", guardada.getId());
        return guardada;
    }
    // → ACTUALIZAR SUCURSAL
    public Sucursal actualizarSucursal(Integer id, SucursalRequestDTO dto) {
        log.info("Actualizando sucursal con id {}", id);
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con id: " + id));

        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada con id: " + dto.getRegionId()));

        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());
        sucursal.setCiudad(dto.getCiudad());
        sucursal.setActiva(dto.getActiva());
        sucursal.setCantidadVehiculos(dto.getCantidadVehiculos());
        sucursal.setRegion(region);

        Sucursal actualizada = sucursalRepository.save(sucursal);
        log.info("Sucursal con id {} actualizada correctamente", id);
        return actualizada;
    }
    //→ ELIMINAR SUCURSAL
    public void eliminarSucursal(Integer id) {
        log.info("Eliminando sucursal con id {}", id);
        if (!sucursalRepository.existsById(id)) {
            log.warn("Intento de eliminar sucursal inexistente con id {}", id);
            throw new ResourceNotFoundException("Sucursal no encontrada con id: " + id);
        }
        sucursalRepository.deleteById(id);
        log.info("Sucursal con id {} eliminada", id);
    }

    // → LISTAR SUCURSALES OPERATIVAS
    public List<Sucursal> obtenerSucursalesOperativas() {
        log.info("Listando sucursales operativas");
        return sucursalRepository.obtenerSucursalesOperativas();
    }
}
