package com.prueba.ms_clientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_clientes.assembler.DireccionModelAssembler;
import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.service.DireccionService;
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

@WebMvcTest(DireccionController.class)
@Import(DireccionModelAssembler.class)
@ActiveProfiles("test")
public class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionService direccionService;

    @Autowired
    private ObjectMapper objectMapper;

    private DireccionDTO direccionDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        direccionDTO = DireccionDTO.builder()
                .id(1)
                .calle("Av. Providencia")
                .numeroCasa(1234)
                .comuna("Providencia")
                .ciudad("Santiago")
                .codigoPostal(7500000)
                .estado(true)
                .fechaRegistro(LocalDate.of(2026, 6, 20))
                .clienteId(1)
                .build();
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/direcciones
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/direcciones → debe retornar 200 con lista de direcciones")
    public void testObtenerDirecciones() throws Exception {
        // GIVEN
        when(direccionService.obtenerDirecciones()).thenReturn(List.of(direccionDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/direcciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.direccionDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.direccionDTOList[0].comuna").value("Providencia"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/direcciones → debe retornar 200 con lista vacía")
    public void testObtenerDireccionesVacio() throws Exception {
        // GIVEN
        when(direccionService.obtenerDirecciones()).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/direcciones"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/direcciones/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/direcciones/{id} → debe retornar 200 cuando existe")
    public void testObtenerDireccionPorId() throws Exception {
        // GIVEN
        when(direccionService.obtenerDireccionPorId(1)).thenReturn(direccionDTO);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/direcciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comuna").value("Providencia"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/direcciones/{id} → debe retornar 404 cuando no existe")
    public void testObtenerDireccionPorIdNoExiste() throws Exception {
        // GIVEN
        when(direccionService.obtenerDireccionPorId(99)).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/direcciones/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/direcciones/comuna/{comuna}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/direcciones/comuna/{comuna} → debe retornar 200 con direcciones encontradas")
    public void testBuscarPorComuna() throws Exception {
        // GIVEN
        when(direccionService.buscarPorComuna("Providencia"))
                .thenReturn(List.of(direccionDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/direcciones/comuna/Providencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/direcciones/comuna/{comuna} → debe retornar 404 si no hay coincidencias")
    public void testBuscarPorComunaVacio() throws Exception {
        // GIVEN
        when(direccionService.buscarPorComuna("NoExiste")).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/direcciones/comuna/NoExiste"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/direcciones
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/direcciones → debe retornar 201 al crear dirección exitosamente")
    public void testGuardarDireccion() throws Exception {
        // GIVEN
        when(direccionService.guardarDireccion(any(DireccionDTO.class)))
                .thenReturn(direccionDTO);

        // WHEN - THEN
        mockMvc.perform(post("/api/v1/direcciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(direccionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/direcciones/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/direcciones/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarDireccion() throws Exception {
        // GIVEN
        when(direccionService.actualizarDireccion(eq(1), any(DireccionDTO.class)))
                .thenReturn(direccionDTO);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/direcciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(direccionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/direcciones/{id} → debe retornar 404 cuando no existe")
    public void testActualizarDireccionNoExiste() throws Exception {
        // GIVEN
        when(direccionService.actualizarDireccion(eq(99), any(DireccionDTO.class)))
                .thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/direcciones/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(direccionDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/direcciones/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/direcciones/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarDireccion() throws Exception {
        // GIVEN
        when(direccionService.eliminarDireccion(1)).thenReturn(true);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/direcciones/1"))
                .andExpect(status().isNoContent());

        verify(direccionService, times(1)).eliminarDireccion(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/direcciones/{id} → debe retornar 404 cuando no existe")
    public void testEliminarDireccionNoExiste() throws Exception {
        // GIVEN
        when(direccionService.eliminarDireccion(99)).thenReturn(false);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/direcciones/99"))
                .andExpect(status().isNotFound());
    }
}