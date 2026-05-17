package com.prueba.ms_sucursales.controller;

import com.prueba.ms_sucursales.dto.RegionDTO;
import com.prueba.ms_sucursales.service.RegionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    // GET → LISTAR TODAS LAS REGIONES
    @GetMapping
    public ResponseEntity<List<RegionDTO>> obtenerRegiones() {
        List<RegionDTO> regiones = regionService.obtenerRegiones();
        return ResponseEntity.ok(regiones);
    }

    // GET → BUSCAR REGION POR ID
    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> obtenerRegionPorId(@PathVariable Integer id) {
        RegionDTO region = regionService.obtenerRegionPorId(id);
        if (region == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(region);
    }

    // POST → CREAR NUEVA REGION
    @PostMapping
    public ResponseEntity<RegionDTO> guardarRegion(@Valid @RequestBody RegionDTO dto) {
        RegionDTO guardada = regionService.guardarRegion(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guardada);
    }

    // PUT → ACTUALIZAR REGION
    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO>
    actualizarRegion(@PathVariable Integer id, @Valid @RequestBody RegionDTO dto) {
        RegionDTO actualizada = regionService.actualizarRegion(id, dto);
        if (actualizada == null) {return ResponseEntity.notFound().build();
        }return ResponseEntity.ok(actualizada);
    }

    // DELETE → ELIMINAR REGION
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegion(
            @PathVariable Integer id) {
        boolean eliminada = regionService.eliminarRegion(id);
        if (!eliminada) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
