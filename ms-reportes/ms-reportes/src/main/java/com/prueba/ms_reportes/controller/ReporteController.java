package com.prueba.ms_reportes.controller;

import com.prueba.ms_reportes.assembler.ReporteModelAssembler;
import com.prueba.ms_reportes.dto.PagoDTO;
import com.prueba.ms_reportes.dto.ReservaDTO;
import com.prueba.ms_reportes.dto.request.ReporteRequestDTO;
import com.prueba.ms_reportes.dto.response.ReporteResponseDTO;
import com.prueba.ms_reportes.model.Reporte;
import com.prueba.ms_reportes.service.ReporteService;
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
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "Gestión y consolidación de reportes de RentaCar")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    private final ReporteService reporteService;
    private final ReporteModelAssembler assembler;

    public ReporteController(ReporteService reporteService, ReporteModelAssembler assembler) {
        this.reporteService = reporteService;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los reportes")
    @GetMapping
    public ResponseEntity<CollectionModel<ReporteResponseDTO>> obtenerReportes() {
        log.info("GET /api/v1/reportes");
        List<Reporte> reportes = reporteService.obtenerReportes();
        CollectionModel<ReporteResponseDTO> collectionModel = assembler.toCollectionModel(reportes)
                .add(linkTo(methodOn(ReporteController.class).obtenerReportes()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Buscar reporte por id")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtenerReportePorId(@PathVariable Integer id) {
        log.info("GET /api/v1/reportes/{}", id);
        Reporte reporte = reporteService.obtenerReportePorId(id);
        return ResponseEntity.ok(assembler.toModel(reporte));
    }

    @Operation(summary = "Crear un nuevo reporte")
    @PostMapping
    public ResponseEntity<ReporteResponseDTO> guardarReporte(@Valid @RequestBody ReporteRequestDTO dto) {
        log.info("POST /api/v1/reportes - titulo={}", dto.getTitulo());
        Reporte guardado = reporteService.guardarReporte(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(guardado));
    }

    @Operation(summary = "Actualizar un reporte existente")
    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> actualizarReporte(
            @PathVariable Integer id, @Valid @RequestBody ReporteRequestDTO dto) {
        log.info("PUT /api/v1/reportes/{}", id);
        Reporte actualizado = reporteService.actualizarReporte(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @Operation(summary = "Eliminar un reporte")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Integer id) {
        log.info("DELETE /api/v1/reportes/{}", id);
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar reportes activos")
    @GetMapping("/activos")
    public ResponseEntity<CollectionModel<ReporteResponseDTO>> obtenerReportesActivos() {
        log.info("GET /api/v1/reportes/activos");
        List<Reporte> reportes = reporteService.obtenerReportesActivos();
        CollectionModel<ReporteResponseDTO> collectionModel = assembler.toCollectionModel(reportes)
                .add(linkTo(methodOn(ReporteController.class).obtenerReportesActivos()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Consultar reservas consolidadas (via Feign a ms-reservas)")
    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaDTO>> obtenerReservas() {
        log.info("GET /api/v1/reportes/reservas");
        return ResponseEntity.ok(reporteService.obtenerReservas());
    }

    @Operation(summary = "Consultar pagos consolidados (via Feign a ms-pagos)")
    @GetMapping("/pagos")
    public ResponseEntity<List<PagoDTO>> obtenerPagos() {
        log.info("GET /api/v1/reportes/pagos");
        return ResponseEntity.ok(reporteService.obtenerPagos());
    }
}
