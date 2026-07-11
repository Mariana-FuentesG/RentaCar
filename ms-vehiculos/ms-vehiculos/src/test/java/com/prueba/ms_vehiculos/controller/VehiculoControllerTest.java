package com.prueba.ms_vehiculos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_vehiculos.assembler.VehiculoModelAssembler;
import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import com.prueba.ms_vehiculos.service.VehiculoService;
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

@WebMvcTest(VehiculoController.class)
@Import(VehiculoModelAssembler.class)
@ActiveProfiles("test")
public class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehiculoService vehiculoService;

    @Autowired
    private ObjectMapper objectMapper;

    private VehiculoDTO vehiculoDTO;

    @BeforeEach
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        vehiculoDTO = VehiculoDTO.builder()
                .id(1)
                .patente("ABCD12")
                .marca("Toyota")
                .modelo("Corolla")
                .precioDiario(50000.0)
                .anio(2023)
                .disponible(true)
                .fechaIngreso(LocalDate.of(2026, 6, 20))
                .categoriaId(1)
                .nombreCategoria("SUV")
                .build();
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/vehiculos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/vehiculos → debe retornar 200 con lista de vehículos")
    public void testObtenerVehiculos() throws Exception {
        // GIVEN
        when(vehiculoService.obtenerVehiculos()).thenReturn(List.of(vehiculoDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/vehiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.vehiculoDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.vehiculoDTOList[0].marca").value("Toyota"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/vehiculos → debe retornar 200 con lista vacía")
    public void testObtenerVehiculosVacio() throws Exception {
        // GIVEN
        when(vehiculoService.obtenerVehiculos()).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/vehiculos"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/vehiculos/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/vehiculos/{id} → debe retornar 200 cuando existe")
    public void testObtenerVehiculoPorId() throws Exception {
        // GIVEN
        when(vehiculoService.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/vehiculos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.marca").value("Toyota"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/vehiculos/{id} → debe retornar 404 cuando no existe")
    public void testObtenerVehiculoPorIdNoExiste() throws Exception {
        // GIVEN
        when(vehiculoService.obtenerVehiculoPorId(99)).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/vehiculos/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/vehiculos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/vehiculos → debe retornar 201 al crear vehículo exitosamente")
    public void testGuardarVehiculo() throws Exception {
        // GIVEN
        when(vehiculoService.guardarVehiculo(any(VehiculoDTO.class))).thenReturn(vehiculoDTO);

        // WHEN - THEN
        mockMvc.perform(post("/api/v1/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/vehiculos/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/vehiculos/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarVehiculo() throws Exception {
        // GIVEN
        when(vehiculoService.actualizarVehiculo(eq(1), any(VehiculoDTO.class)))
                .thenReturn(vehiculoDTO);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/vehiculos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/vehiculos/{id} → debe retornar 404 cuando no existe")
    public void testActualizarVehiculoNoExiste() throws Exception {
        // GIVEN
        when(vehiculoService.actualizarVehiculo(eq(99), any(VehiculoDTO.class)))
                .thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/vehiculos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculoDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/vehiculos/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/vehiculos/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarVehiculo() throws Exception {
        // GIVEN
        when(vehiculoService.eliminarVehiculo(1)).thenReturn(true);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/vehiculos/1"))
                .andExpect(status().isNoContent());

        verify(vehiculoService, times(1)).eliminarVehiculo(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/vehiculos/{id} → debe retornar 404 cuando no existe")
    public void testEliminarVehiculoNoExiste() throws Exception {
        // GIVEN
        when(vehiculoService.eliminarVehiculo(99)).thenReturn(false);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/vehiculos/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/vehiculos/buscar
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/vehiculos/buscar → debe retornar 200 con vehículos en el rango")
    public void testObtenerVehiculosDisponiblesPorPrecio() throws Exception {
        // GIVEN
        when(vehiculoService.obtenerVehiculosDisponiblesPorPrecio(100000.0))
                .thenReturn(List.of(vehiculoDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/vehiculos/buscar")
                        .param("precio", "100000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/vehiculos/buscar → debe retornar 200 con lista vacía")
    public void testObtenerVehiculosDisponiblesPorPrecioVacio() throws Exception {
        // GIVEN
        when(vehiculoService.obtenerVehiculosDisponiblesPorPrecio(1000.0))
                .thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/vehiculos/buscar")
                        .param("precio", "1000.0"))
                .andExpect(status().isOk());
    }
}