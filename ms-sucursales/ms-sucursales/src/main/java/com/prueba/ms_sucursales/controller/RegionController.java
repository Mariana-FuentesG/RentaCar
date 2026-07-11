package com.prueba.ms_sucursales.controller;

import com.prueba.ms_sucursales.assembler.RegionModelAssembler;
import com.prueba.ms_sucursales.dto.request.RegionRequestDTO;
import com.prueba.ms_sucursales.dto.response.RegionResponseDTO;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/regiones")
@Tag(name = "Regiones", description = "Gestión de regiones de RentaCar")
public class RegionController {

    private static final Logger log = LoggerFactory.getLogger(RegionController.class);

    private final RegionService regionService;
    private final RegionModelAssembler assembler;

    public RegionController(RegionService regionService, RegionModelAssembler assembler) {
        this.regionService = regionService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todas las regiones")
    @GetMapping
    public ResponseEntity<CollectionModel<RegionResponseDTO>> obtenerRegiones() {
        log.info("GET /api/v1/regiones");
        List<Region> regiones = regionService.obtenerRegiones();
        CollectionModel<RegionResponseDTO> collectionModel = assembler.toCollectionModel(regiones)
                .add(linkTo(methodOn(RegionController.class).obtenerRegiones()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Buscar region por id")
    @GetMapping("/{id}")
    public ResponseEntity<RegionResponseDTO> obtenerRegionPorId(@PathVariable Integer id) {
        log.info("GET /api/v1/regiones/{}", id);
        Region region = regionService.obtenerRegionPorId(id);
        return ResponseEntity.ok(assembler.toModel(region));
    }

    @Operation(summary = "Crear una nueva region")
    @PostMapping
    public ResponseEntity<RegionResponseDTO> guardarRegion(@Valid @RequestBody RegionRequestDTO dto) {
        log.info("POST /api/v1/regiones - nombre={}", dto.getNombre());
        Region guardada = regionService.guardarRegion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(guardada));
    }

    @Operation(summary = "Actualizar una region existente")
    @PutMapping("/{id}")
    public ResponseEntity<RegionResponseDTO> actualizarRegion(
            @PathVariable Integer id, @Valid @RequestBody RegionRequestDTO dto) {
        log.info("PUT /api/v1/regiones/{}", id);
        Region actualizada = regionService.actualizarRegion(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @Operation(summary = "Eliminar una region")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRegion(@PathVariable Integer id) {
        log.info("DELETE /api/v1/regiones/{}", id);
        regionService.eliminarRegion(id);
        return ResponseEntity.noContent().build();
    }
}
