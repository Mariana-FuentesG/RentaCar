package com.prueba.ms_reportes.controller;

import com.prueba.ms_reportes.dto.PagoDTO;
import com.prueba.ms_reportes.dto.ReporteDTO;
import com.prueba.ms_reportes.dto.ReservaDTO;
import com.prueba.ms_reportes.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {
    @Autowired
    ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> obtenerReportes(){
        List<ReporteDTO> reporte = reporteService.obtenerReportes();
        return ResponseEntity.ok(reporte);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtenerReportePorId(@PathVariable Integer id){
        ReporteDTO reporte = reporteService.obtenerReportePorId(id);
        if(reporte == null){return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reporte);
    }

    @PostMapping
    public ResponseEntity<ReporteDTO> guardarReporte(
            @Valid @RequestBody ReporteDTO dto){
        ReporteDTO guardado = reporteService.guardarReporte(dto);
        if(guardado == null){return ResponseEntity.badRequest().build();
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO>
    actualizarReporte(@PathVariable Integer id, @Valid @RequestBody ReporteDTO dto){
        ReporteDTO actualizado = reporteService.actualizarReporte(id, dto);
        if(actualizado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Integer id){
        boolean eliminado = reporteService.eliminarReporte(id);
        if(!eliminado){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ReporteDTO>> obtenerReportesActivos(){
        return ResponseEntity.ok(
                reporteService.obtenerReportesActivos());
    }

    // FEIGN → OBTENER RESERVAS
    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaDTO>> obtenerReservas(){
        return ResponseEntity.ok(reporteService.obtenerReservas());
    }

    // FEIGN → OBTENER PAGOS
    @GetMapping("/pagos")
    public ResponseEntity<List<PagoDTO>> obtenerPagos(){
        return ResponseEntity.ok(reporteService.obtenerPagos());
    }

}
