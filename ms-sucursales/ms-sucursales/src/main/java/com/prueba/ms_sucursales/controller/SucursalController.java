package com.prueba.ms_sucursales.controller;

import com.prueba.ms_sucursales.assembler.SucursalModelAssembler;
import com.prueba.ms_sucursales.dto.request.SucursalRequestDTO;
import com.prueba.ms_sucursales.dto.response.SucursalResponseDTO;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.service.SucursalService;
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

/**
 * Solo administra solicitudes/respuestas HTTP. Toda la lógica vive en el Service,
 * y la construcción de enlaces HATEOAS vive en el Assembler (nunca aquí).
 */
@RestController
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Sucursales", description = "Gestión de sucursales de RentaCar")
public class SucursalController {

    private static final Logger log = LoggerFactory.getLogger(SucursalController.class);

    private final SucursalService sucursalService;
    private final SucursalModelAssembler assembler;

    public SucursalController(SucursalService sucursalService, SucursalModelAssembler assembler) {
        this.sucursalService = sucursalService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todas las sucursales")
    @GetMapping
    public ResponseEntity<CollectionModel<SucursalResponseDTO>> obtenerSucursales() {
        log.info("GET /api/v1/sucursales");
        List<Sucursal> sucursales = sucursalService.obtenerSucursales();
        CollectionModel<SucursalResponseDTO> collectionModel = assembler.toCollectionModel(sucursales)
                .add(linkTo(methodOn(SucursalController.class).obtenerSucursales()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Listar sucursales operativas (activas)")
    @GetMapping("/operativas")
    public ResponseEntity<CollectionModel<SucursalResponseDTO>> obtenerSucursalesOperativas() {
        log.info("GET /api/v1/sucursales/operativas");
        List<Sucursal> sucursales = sucursalService.obtenerSucursalesOperativas();
        CollectionModel<SucursalResponseDTO> collectionModel = assembler.toCollectionModel(sucursales)
                .add(linkTo(methodOn(SucursalController.class).obtenerSucursalesOperativas()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Buscar sucursal por id")
    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> obtenerSucursalPorId(@PathVariable Integer id) {
        log.info("GET /api/v1/sucursales/{}", id);
        Sucursal sucursal = sucursalService.obtenerSucursalPorId(id);
        return ResponseEntity.ok(assembler.toModel(sucursal));
    }

    @Operation(summary = "Crear una nueva sucursal")
    @PostMapping
    public ResponseEntity<SucursalResponseDTO> guardarSucursal(@Valid @RequestBody SucursalRequestDTO dto) {
        log.info("POST /api/v1/sucursales - nombre={}", dto.getNombre());
        Sucursal guardada = sucursalService.guardarSucursal(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(guardada));
    }

    @Operation(summary = "Actualizar una sucursal existente")
    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponseDTO> actualizarSucursal(
            @PathVariable Integer id, @Valid @RequestBody SucursalRequestDTO dto) {
        log.info("PUT /api/v1/sucursales/{}", id);
        Sucursal actualizada = sucursalService.actualizarSucursal(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }

    @Operation(summary = "Eliminar una sucursal")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Integer id) {
        log.info("DELETE /api/v1/sucursales/{}", id);
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
