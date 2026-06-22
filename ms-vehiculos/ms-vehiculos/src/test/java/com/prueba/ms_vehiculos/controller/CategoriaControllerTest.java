package com.prueba.ms_vehiculos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.ms_vehiculos.assembler.CategoriaModelAssembler;
import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import com.prueba.ms_vehiculos.service.CategoriaService;
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

@WebMvcTest(CategoriaController.class)
@Import(CategoriaModelAssembler.class)
@ActiveProfiles("test")
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        categoriaDTO = CategoriaDTO.builder()
                .id(1)
                .nombre("SUV")
                .capacidadPasajeros(5)
                .activa(true)
                .fechaCreacion(LocalDate.of(2026, 6, 20))
                .precioBase(80000.0)
                .build();
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/categorias
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/categorias → debe retornar 200 con lista de categorías")
    public void testObtenerCategorias() throws Exception {
        // GIVEN
        when(categoriaService.obtenerCategorias()).thenReturn(List.of(categoriaDTO));

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categoriaDTOList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.categoriaDTOList[0].nombre").value("SUV"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/categorias → debe retornar 200 con lista vacía")
    public void testObtenerCategoriasVacio() throws Exception {
        // GIVEN
        when(categoriaService.obtenerCategorias()).thenReturn(List.of());

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/categorias/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/categorias/{id} → debe retornar 200 cuando existe")
    public void testObtenerCategoriaPorId() throws Exception {
        // GIVEN
        when(categoriaService.obtenerCategoriaPorId(1)).thenReturn(categoriaDTO);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("SUV"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("GET /api/v1/categorias/{id} → debe retornar 404 cuando no existe")
    public void testObtenerCategoriaPorIdNoExiste() throws Exception {
        // GIVEN
        when(categoriaService.obtenerCategoriaPorId(99)).thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(get("/api/v1/categorias/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/categorias
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/v1/categorias → debe retornar 201 al crear categoría exitosamente")
    public void testGuardarCategoria() throws Exception {
        // GIVEN
        when(categoriaService.guardarCategoria(any(CategoriaDTO.class)))
                .thenReturn(categoriaDTO);

        // WHEN - THEN
        mockMvc.perform(post("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/categorias/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/v1/categorias/{id} → debe retornar 200 al actualizar correctamente")
    public void testActualizarCategoria() throws Exception {
        // GIVEN
        when(categoriaService.actualizarCategoria(eq(1), any(CategoriaDTO.class)))
                .thenReturn(categoriaDTO);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    @DisplayName("PUT /api/v1/categorias/{id} → debe retornar 404 cuando no existe")
    public void testActualizarCategoriaNoExiste() throws Exception {
        // GIVEN
        when(categoriaService.actualizarCategoria(eq(99), any(CategoriaDTO.class)))
                .thenReturn(null);

        // WHEN - THEN
        mockMvc.perform(put("/api/v1/categorias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/categorias/{id}
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/v1/categorias/{id} → debe retornar 204 al eliminar correctamente")
    public void testEliminarCategoria() throws Exception {
        // GIVEN
        when(categoriaService.eliminarCategoria(1)).thenReturn(true);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).eliminarCategoria(1);
    }

    @Test
    @DisplayName("DELETE /api/v1/categorias/{id} → debe retornar 404 cuando no existe")
    public void testEliminarCategoriaNoExiste() throws Exception {
        // GIVEN
        when(categoriaService.eliminarCategoria(99)).thenReturn(false);

        // WHEN - THEN
        mockMvc.perform(delete("/api/v1/categorias/99"))
                .andExpect(status().isNotFound());
    }
}