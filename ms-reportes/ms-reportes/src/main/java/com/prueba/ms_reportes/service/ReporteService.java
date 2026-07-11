package com.prueba.ms_reportes.service;

import com.prueba.ms_reportes.client.PagoClient;
import com.prueba.ms_reportes.client.ReservaClient;
import com.prueba.ms_reportes.dto.PagoDTO;
import com.prueba.ms_reportes.dto.ReservaDTO;
import com.prueba.ms_reportes.dto.request.ReporteRequestDTO;
import com.prueba.ms_reportes.exception.ResourceNotFoundException;
import com.prueba.ms_reportes.mapper.ReporteMapper;
import com.prueba.ms_reportes.model.Reporte;
import com.prueba.ms_reportes.repository.ReporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Trabaja con entidades; la conversión a DTO + enlaces HATEOAS la hace el assembler.
 */
@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final ReporteRepository reporteRepository;
    private final ReservaClient reservaClient;
    private final PagoClient pagoClient;

    public ReporteService(ReporteRepository reporteRepository, ReservaClient reservaClient, PagoClient pagoClient) {
        this.reporteRepository = reporteRepository;
        this.reservaClient = reservaClient;
        this.pagoClient = pagoClient;
    }

    public List<Reporte> obtenerReportes() {
        log.info("Listando todos los reportes");
        return reporteRepository.findAll();
    }

    public Reporte obtenerReportePorId(Integer id) {
        log.info("Buscando reporte con id {}", id);
        return reporteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reporte con id {} no encontrado", id);
                    return new ResourceNotFoundException("Reporte no encontrado con id: " + id);
                });
    }

    public Reporte guardarReporte(ReporteRequestDTO dto) {
        log.info("Creando nuevo reporte: {}", dto.getTitulo());
        Reporte reporte = ReporteMapper.toEntity(dto);
        Reporte guardado = reporteRepository.save(reporte);
        log.info("Reporte creado con id {}", guardado.getId());
        return guardado;
    }

    public Reporte actualizarReporte(Integer id, ReporteRequestDTO dto) {
        log.info("Actualizando reporte con id {}", id);
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con id: " + id));

        reporte.setTitulo(dto.getTitulo());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setTotalReservas(dto.getTotalReservas());
        reporte.setTotalIngresos(dto.getTotalIngresos());
        reporte.setActivo(dto.getActivo());
        reporte.setFechaGeneracion(dto.getFechaGeneracion());

        Reporte actualizado = reporteRepository.save(reporte);
        log.info("Reporte con id {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminarReporte(Integer id) {
        log.info("Eliminando reporte con id {}", id);
        if (!reporteRepository.existsById(id)) {
            log.warn("Intento de eliminar reporte inexistente con id {}", id);
            throw new ResourceNotFoundException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
        log.info("Reporte con id {} eliminado", id);
    }

    public List<Reporte> obtenerReportesActivos() {
        log.info("Listando reportes activos");
        return reporteRepository.findByActivoTrue();
    }

    public List<ReservaDTO> obtenerReservas() {
        log.info("Consultando reservas via Feign a ms-reservas");
        return reservaClient.obtenerReservas();
    }

    public List<PagoDTO> obtenerPagos() {
        log.info("Consultando pagos via Feign a ms-pagos");
        return pagoClient.obtenerPagos();
    }
}
