package com.prueba.ms_clientes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_clientes.assembler.ClienteModelAssembler;
import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.service.ClienteService;
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

@WebMvcTest(ClienteController.class)
@Import(ClienteModelAssembler.class)
@ActiveProfiles("test")
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteDTO clienteDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1);
        clienteDTO.setRut(12345678);
        clienteDTO.setNombreCompleto("Juan Pérez");
        clienteDTO.setEmail("juan@example.com");
        clienteDTO.setTelefono("912345678");
        clienteDTO.setActivo(true);
        clienteDTO.setFechaRegistro(LocalDate.of(2026, 6, 20));
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/clientes
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/clientes → debe retornar 200 con lista de clientes")
    public void testListarClientes() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientes()).thenReturn(List.of(clienteDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.clienteDTOList[0].nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/clientes → debe retornar 200 con lista vacía")
    public void testListarClientesVacio() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientes()).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/clientes/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/clientes/{id} → debe retornar 200 cuando existe")
    public void testObtenerClientePorId() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientePorId(1)).thenReturn(clienteDTO);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Pérez"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/clientes/{id} → debe retornar 404 cuando no existe")
    public void testObtenerClientePorIdNoExiste() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientePorId(99)).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/clientes/email/{email}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/clientes/email/{email} → debe retornar 200 con clientes encontrados")
    public void testObtenerClientePorEmail() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientesPorEmail("juan@example.com"))
                .thenReturn(List.of(clienteDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes/email/juan@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/clientes/email/{email} → debe retornar 404 si no hay coincidencias")
    public void testObtenerClientePorEmailNoExiste() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientesPorEmail("noexiste@mail.com"))
                .thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes/email/noexiste@mail.com"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/clientes/activos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/clientes/activos → debe retornar 200 con clientes activos")
    public void testObtenerClientesActivos() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientesActivos()).thenReturn(List.of(clienteDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/clientes/activos → debe retornar 404 si no hay clientes activos")
    public void testObtenerClientesActivosVacio() throws Exception {
        // GIVEN
        when(clienteService.obtenerClientesActivos()).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/clientes/activos"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/clientes
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/clientes → debe retornar 201 al crear cliente exitosamente")
    public void testGuardarCliente() throws Exception {
        // GIVEN
        when(clienteService.guardarCliente(any(ClienteDTO.class))).thenReturn(clienteDTO);

        // WHEN - THEN
        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/clientes/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/clientes/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarCliente() throws Exception {
        // GIVEN
        when(clienteService.actualizarCliente(eq(1), any(ClienteDTO.class)))
                .thenReturn(clienteDTO);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/clientes/{id} → debe retornar 404 cuando el cliente no existe")
    public void testActualizarClienteNoExiste() throws Exception {
        // GIVEN
        when(clienteService.actualizarCliente(eq(99), any(ClienteDTO.class)))
                .thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/clientes/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/clientes/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarCliente() throws Exception {
        // GIVEN
        when(clienteService.eliminarClienteId(1)).thenReturn(true);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).eliminarClienteId(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/clientes/{id} → debe retornar 404 cuando el cliente no existe")
    public void testEliminarClienteNoExiste() throws Exception {
        // GIVEN
        when(clienteService.eliminarClienteId(99)).thenReturn(false);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/clientes/99"))
                .andExpect(status().isNotFound());
    }
}