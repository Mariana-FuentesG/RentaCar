package com.prueba.ms_pagos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_pagos.assembler.PagoModelAssembler;
import com.prueba.ms_pagos.dto.PagoDTO;
import com.prueba.ms_pagos.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
@Import(PagoModelAssembler.class)
@ActiveProfiles("test")
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PagoDTO pagoDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        pagoDTO = PagoDTO.builder()
                .id(1)
                .reservaId(1)
                .monto(150000.0)
                .pagado(true)
                .fechaPago(LocalDate.of(2026, 6, 16))
                .metodoPago("Tarjeta de Crédito")
                .cantCuotas(1)
                .build();
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/pagos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/pagos → debe retornar 200 con lista de pagos")
    public void testObtenerPagos() throws Exception {
        // GIVEN
        when(pagoService.obtenerPagos()).thenReturn(List.of(pagoDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pagoDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.pagoDTOList[0].monto").value(150000.0))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/pagos → debe retornar 200 con lista vacía")
    public void testObtenerPagosVacio() throws Exception {
        // GIVEN
        when(pagoService.obtenerPagos()).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/pagos/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/pagos/{id} → debe retornar 200 cuando existe")
    public void testObtenerPagoPorId() throws Exception {
        // GIVEN
        when(pagoService.obtenerPagoPorId(1)).thenReturn(pagoDTO);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.monto").value(150000.0))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/pagos/{id} → debe retornar 404 cuando no existe")
    public void testObtenerPagoPorIdNoExiste() throws Exception {
        // GIVEN
        when(pagoService.obtenerPagoPorId(99)).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/pagos/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/pagos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/pagos → debe retornar 201 al crear pago exitosamente")
    public void testGuardarPago() throws Exception {
        // GIVEN
        when(pagoService.guardarPago(any(PagoDTO.class))).thenReturn(pagoDTO);

        // WHEN - THEN
        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("POST /api/v1/pagos → debe retornar 400 cuando la reserva no existe")
    public void testGuardarPagoReservaNoExiste() throws Exception {
        // GIVEN
        when(pagoService.guardarPago(any(PagoDTO.class))).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(post("/api/v1/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/pagos/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/pagos/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarPago() throws Exception {
        // GIVEN
        when(pagoService.actualizarPago(eq(1), any(PagoDTO.class))).thenReturn(pagoDTO);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/pagos/{id} → debe retornar 404 cuando el pago no existe")
    public void testActualizarPagoNoExiste() throws Exception {
        // GIVEN
        when(pagoService.actualizarPago(eq(99), any(PagoDTO.class))).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/pagos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagoDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/pagos/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/pagos/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarPago() throws Exception {
        // GIVEN
        when(pagoService.eliminarPago(1)).thenReturn(true);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/pagos/1"))
                .andExpect(status().isNoContent());

        verify(pagoService, times(1)).eliminarPago(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/pagos/{id} → debe retornar 404 cuando el pago no existe")
    public void testEliminarPagoNoExiste() throws Exception {
        // GIVEN
        when(pagoService.eliminarPago(99)).thenReturn(false);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/pagos/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/pagos/buscar
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/pagos/buscar → debe retornar 200 con pagos en el rango")
    public void testBuscarPagosPorMonto() throws Exception {
        // GIVEN
        when(pagoService.buscarPagosPorMonto(50000.0, 200000.0))
                .thenReturn(List.of(pagoDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/pagos/buscar")
                        .param("minimo", "50000.0")
                        .param("maximo", "200000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/pagos/buscar → debe retornar 200 con lista vacía")
    public void testBuscarPagosPorMontoVacio() throws Exception {
        // GIVEN
        when(pagoService.buscarPagosPorMonto(1000.0, 2000.0))
                .thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/pagos/buscar")
                        .param("minimo", "1000.0")
                        .param("maximo", "2000.0"))
                .andExpect(status().isOk());
    }
}