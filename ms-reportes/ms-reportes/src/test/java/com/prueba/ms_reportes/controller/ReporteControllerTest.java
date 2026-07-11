package com.prueba.ms_reportes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_reportes.assembler.ReporteModelAssembler;
import com.prueba.ms_reportes.dto.request.ReporteRequestDTO;
import com.prueba.ms_reportes.dto.response.ReporteResponseDTO;
import com.prueba.ms_reportes.exception.ResourceNotFoundException;
import com.prueba.ms_reportes.model.Reporte;
import com.prueba.ms_reportes.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReporteService reporteService;

    @MockitoBean
    private ReporteModelAssembler assembler;

    private Reporte buildReporte() {
        Reporte reporte = new Reporte();
        reporte.setId(1);
        reporte.setTitulo("Reporte Mensual");
        reporte.setDescripcion("Reporte mensual de reservas");
        reporte.setTotalReservas(25);
        reporte.setTotalIngresos(5500000.0);
        reporte.setActivo(true);
        reporte.setFechaGeneracion(LocalDate.now());
        return reporte;
    }

    private ReporteResponseDTO buildResponseDTO(Reporte reporte) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setId(reporte.getId());
        dto.setTitulo(reporte.getTitulo());
        dto.setDescripcion(reporte.getDescripcion());
        dto.setTotalReservas(reporte.getTotalReservas());
        dto.setTotalIngresos(reporte.getTotalIngresos());
        dto.setActivo(reporte.getActivo());
        dto.setFechaGeneracion(reporte.getFechaGeneracion());
        return dto;
    }

    @Test
    void debeRetornarReportePorId() throws Exception {
        Reporte reporte = buildReporte();
        when(reporteService.obtenerReportePorId(1)).thenReturn(reporte);
        when(assembler.toModel(reporte)).thenReturn(buildResponseDTO(reporte));

        mockMvc.perform(get("/api/v1/reportes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Reporte Mensual"));
    }

    @Test
    void debeRetornar404SiReporteNoExiste() throws Exception {
        when(reporteService.obtenerReportePorId(99))
                .thenThrow(new ResourceNotFoundException("Reporte no encontrado con id: 99"));

        mockMvc.perform(get("/api/v1/reportes/{id}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void debeCrearReporte() throws Exception {
        ReporteRequestDTO request = new ReporteRequestDTO(
                "Reporte Nuevo", "Descripcion valida", 5, 100000.0, true, LocalDate.now());
        Reporte reporte = buildReporte();
        when(reporteService.guardarReporte(any(ReporteRequestDTO.class))).thenReturn(reporte);
        when(assembler.toModel(reporte)).thenReturn(buildResponseDTO(reporte));

        mockMvc.perform(post("/api/v1/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeRetornar400SiFaltaCampoObligatorio() throws Exception {
        ReporteRequestDTO request = new ReporteRequestDTO(
                "", "Descripcion valida", 5, 100000.0, true, LocalDate.now());

        mockMvc.perform(post("/api/v1/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void debeEliminarReporte() throws Exception {
        mockMvc.perform(delete("/api/v1/reportes/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeRetornarReservasDelegandoAlService() throws Exception {
        when(reporteService.obtenerReservas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reportes/reservas"))
                .andExpect(status().isOk());
    }
}
