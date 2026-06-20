package com.prueba.ms_reservas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_reservas.assembler.EstadoReservaModelAssembler;
import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import com.prueba.ms_reservas.service.EstadoReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoReservaController.class)
@Import(EstadoReservaModelAssembler.class)
public class EstadoReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EstadoReservaService estadoReservaService;

    @Autowired
    private ObjectMapper objectMapper;

    private EstadoReservaDTO estadoDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        estadoDTO = new EstadoReservaDTO();
        estadoDTO.setId(1);
        estadoDTO.setNombreEstado("Pendiente");
        estadoDTO.setPrioridad(1);
        estadoDTO.setActivo(true);
        estadoDTO.setFechaCreacion(LocalDate.of(2026, 6, 16));
        estadoDTO.setDiasLimitePago(3);
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/estados-reserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/estados-reserva → debe retornar 200 con lista de estados")
    public void testObtenerEstadosReserva() throws Exception {
        // GIVEN
        when(estadoReservaService.obtenerEstadosReserva()).thenReturn(List.of(estadoDTO));
        // WHEN - THEN
        mockMvc.perform(get("/api/v1/estados-reserva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.estadoReservaDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.estadoReservaDTOList[0].nombreEstado").value("Pendiente"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/estados-reserva → debe retornar 200 con lista vacía")
    public void testObtenerEstadosReservaVacio() throws Exception {
        when(estadoReservaService.obtenerEstadosReserva()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/estados-reserva"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/estados-reserva/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/estados-reserva/{id} → debe retornar 200 cuando existe")
    public void testObtenerEstadoReservaPorId() throws Exception {
        when(estadoReservaService.obtenerEstadoReservaPorId(1)).thenReturn(estadoDTO);

        mockMvc.perform(get("/api/v1/estados-reserva/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreEstado").value("Pendiente"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/estados-reserva/{id} → debe retornar 404 cuando no existe")
    public void testObtenerEstadoReservaPorIdNoExiste() throws Exception {
        when(estadoReservaService.obtenerEstadoReservaPorId(99)).thenReturn(null);

        mockMvc.perform(get("/api/v1/estados-reserva/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/estados-reserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/estados-reserva → debe retornar 201 al crear estado correctamente")
    public void testGuardarEstadoReserva() throws Exception {
        when(estadoReservaService.guardarEstadoReserva(any(EstadoReservaDTO.class)))
                .thenReturn(estadoDTO);

        mockMvc.perform(post("/api/v1/estados-reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreEstado").value("Pendiente"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/estados-reserva/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/estados-reserva/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarEstadoReserva() throws Exception {
        when(estadoReservaService.actualizarEstadoReserva(eq(1), any(EstadoReservaDTO.class)))
                .thenReturn(estadoDTO);

        mockMvc.perform(put("/api/v1/estados-reserva/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/estados-reserva/{id} → debe retornar 404 cuando no existe")
    public void testActualizarEstadoReservaNoExiste() throws Exception {
        when(estadoReservaService.actualizarEstadoReserva(eq(99), any(EstadoReservaDTO.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/estados-reserva/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/estados-reserva/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/estados-reserva/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarEstadoReserva() throws Exception {
        when(estadoReservaService.eliminarEstadoReserva(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/estados-reserva/1"))
                .andExpect(status().isNoContent());

        verify(estadoReservaService, times(1)).eliminarEstadoReserva(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/estados-reserva/{id} → debe retornar 404 cuando no existe")
    public void testEliminarEstadoReservaNoExiste() throws Exception {
        when(estadoReservaService.eliminarEstadoReserva(99)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/estados-reserva/99"))
                .andExpect(status().isNotFound());
    }
}
