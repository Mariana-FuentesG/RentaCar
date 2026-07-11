package com.prueba.ms_pagos.controller;

import com.prueba.ms_pagos.assembler.PagoModelAssembler;
import com.prueba.ms_pagos.dto.PagoDTO;
import com.prueba.ms_pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos")
@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoModelAssembler assembler;

    // GET → LISTAR TODOS LOS PAGOS
    @Operation(summary = "Listar todos los pagos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> obtenerPagos() {
        List<PagoDTO> pagos = pagoService.obtenerPagos();
        CollectionModel<EntityModel<PagoDTO>> modelo = assembler.toCollectionModel(pagos);
        return ResponseEntity.ok(modelo);
    }

    // GET → BUSCAR PAGO POR ID
    @Operation(summary = "Obtener pago por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoDTO>> obtenerPagoPorId(
            @Parameter(description = "ID del pago") @PathVariable Integer id) {
        PagoDTO pago = pagoService.obtenerPagoPorId(id);
        if (pago == null) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(assembler.toModel(pago));
    }

    // POST → GUARDAR PAGO
    @Operation(summary = "Crear un nuevo pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<PagoDTO>> guardarPago(@Valid @RequestBody PagoDTO dto) {
        PagoDTO guardado = pagoService.guardarPago(dto);
        if (guardado == null) { return ResponseEntity.badRequest().build(); }
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(guardado));
    }

    // PUT → ACTUALIZAR PAGO
    @Operation(summary = "Actualizar un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PagoDTO>> actualizarPago(
            @Parameter(description = "ID del pago") @PathVariable Integer id,
            @Valid @RequestBody PagoDTO dto) {
        PagoDTO actualizado = pagoService.actualizarPago(id, dto);
        if (actualizado == null) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    // DELETE → ELIMINAR PAGO
    @Operation(summary = "Eliminar un pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago") @PathVariable Integer id) {
        boolean eliminado = pagoService.eliminarPago(id);
        if (!eliminado) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.noContent().build();
    }

    // JPQL → BUSCAR PAGOS POR MONTO
    @Operation(summary = "Buscar pagos por rango de monto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagos encontrados en el rango")
    })
    @GetMapping("/buscar")
    public ResponseEntity<CollectionModel<EntityModel<PagoDTO>>> buscarPagosPorMonto(
            @Parameter(description = "Monto mínimo") @RequestParam Double minimo,
            @Parameter(description = "Monto máximo") @RequestParam Double maximo) {
        List<PagoDTO> pagos = pagoService.buscarPagosPorMonto(minimo, maximo);
        return ResponseEntity.ok(assembler.toCollectionModel(pagos));
    }
}