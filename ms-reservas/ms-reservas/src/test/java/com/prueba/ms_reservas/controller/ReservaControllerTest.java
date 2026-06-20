package com.prueba.ms_reservas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
@ActiveProfiles("test")
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReservaDTO reservaDTO;

    @BeforeEach
    void setUp() {
        reservaDTO = new ReservaDTO();
        reservaDTO.setId(1);
        reservaDTO.setClienteId(1);
        reservaDTO.setVehiculoId(1);
        reservaDTO.setMontoReserva(150000.0);
        reservaDTO.setCantidadDias(3);
        reservaDTO.setPagada(false);
        reservaDTO.setFechaInicio(LocalDate.of(2026, 6, 20));
        reservaDTO.setFechaTermino(LocalDate.of(2026, 6, 23));
        reservaDTO.setFechaReserva(LocalDate.of(2026, 6, 19));
        reservaDTO.setObservacion("Cliente solicita entrega en sucursal central");
        reservaDTO.setEstadoReservaId(1);
        reservaDTO.setNombreEstado("Pendiente");
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/reservas
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/reservas → debe retornar 200 con lista de reservas")
    public void testObtenerReservas() throws Exception {
        when(reservaService.obtenerReservas()).thenReturn(List.of(reservaDTO));

        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reservaDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.reservaDTOList[0].clienteId").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/reservas → debe retornar 200 con lista vacía")
    public void testObtenerReservasVacio() throws Exception {
        when(reservaService.obtenerReservas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/reservas/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/reservas/{id} → debe retornar 200 cuando existe")
    public void testObtenerReservaPorId() throws Exception {
        when(reservaService.obtenerReservaPorId(1)).thenReturn(reservaDTO);

        mockMvc.perform(get("/api/v1/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/reservas/{id} → debe retornar 404 cuando no existe")
    public void testObtenerReservaPorIdNoExiste() throws Exception {
        when(reservaService.obtenerReservaPorId(99)).thenReturn(null);

        mockMvc.perform(get("/api/v1/reservas/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/reservas
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/reservas → debe retornar 201 al crear reserva exitosamente")
    public void testGuardarReserva() throws Exception {
        when(reservaService.guardarReserva(any(ReservaDTO.class))).thenReturn(reservaDTO);

        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("POST /api/v1/reservas → debe retornar 400 cuando cliente o vehículo no existe")
    public void testGuardarReservaClienteNoExiste() throws Exception {
        when(reservaService.guardarReserva(any(ReservaDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/reservas/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/reservas/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarReserva() throws Exception {
        when(reservaService.actualizarReserva(eq(1), any(ReservaDTO.class))).thenReturn(reservaDTO);

        mockMvc.perform(put("/api/v1/reservas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/reservas/{id} → debe retornar 404 cuando la reserva no existe")
    public void testActualizarReservaNoExiste() throws Exception {
        when(reservaService.actualizarReserva(eq(99), any(ReservaDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/reservas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/reservas/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/reservas/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarReserva() throws Exception {
        when(reservaService.eliminarReserva(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/reservas/1"))
                .andExpect(status().isNoContent());

        verify(reservaService, times(1)).eliminarReserva(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/reservas/{id} → debe retornar 404 cuando la reserva no existe")
    public void testEliminarReservaNoExiste() throws Exception {
        when(reservaService.eliminarReserva(99)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/reservas/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/reservas/fecha/{fecha}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/reservas/fecha/{fecha} → debe retornar 200 con reservas encontradas")
    public void testBuscarReservasDesdeFecha() throws Exception {
        when(reservaService.buscarReservasDesdeFecha(any(LocalDate.class)))
                .thenReturn(List.of(reservaDTO));

        mockMvc.perform(get("/api/v1/reservas/fecha/2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/reservas/fecha/{fecha} → debe retornar 404 si no hay reservas")
    public void testBuscarReservasDesdeFechaVacio() throws Exception {
        when(reservaService.buscarReservasDesdeFecha(any(LocalDate.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reservas/fecha/2026-01-01"))
                .andExpect(status().isNotFound());
    }
}
