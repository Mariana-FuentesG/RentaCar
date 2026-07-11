package com.prueba.ms_vehiculos.service;

import com.prueba.ms_vehiculos.dto.CategoriaDTO;
import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.repository.CategoriaRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @MockitoBean
    private CategoriaRepository categoriaRepository;

    private final Faker faker = new Faker();

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre(faker.options().option("SUV", "Sedan", "Hatchback"));
        categoria.setCapacidadPasajeros(faker.number().numberBetween(2, 8));
        categoria.setActiva(true);
        categoria.setFechaCreacion(LocalDate.now());
        categoria.setPrecioBase(faker.number().randomDouble(2, 50000, 200000));

        categoriaDTO = CategoriaDTO.builder()
                .id(1)
                .nombre(categoria.getNombre())
                .capacidadPasajeros(categoria.getCapacidadPasajeros())
                .activa(true)
                .fechaCreacion(LocalDate.now())
                .precioBase(categoria.getPrecioBase())
                .build();
    }

    // ─────────────────────────────────────────────
    // obtenerCategorias
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista con todas las categorías")
    public void testObtenerCategorias() {
        // GIVEN
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        // WHEN
        List<CategoriaDTO> resultado = categoriaService.obtenerCategorias();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay categorías")
    public void testObtenerCategoriasVacio() {
        // GIVEN
        when(categoriaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<CategoriaDTO> resultado = categoriaService.obtenerCategorias();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerCategoriaPorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar una categoría existente por ID")
    public void testObtenerCategoriaPorId() {
        // GIVEN
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        // WHEN
        CategoriaDTO resultado = categoriaService.obtenerCategoriaPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(categoriaRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe retornar null si la categoría no existe")
    public void testObtenerCategoriaPorIdNoExiste() {
        // GIVEN
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        CategoriaDTO resultado = categoriaService.obtenerCategoriaPorId(99);

        // THEN
        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // guardarCategoria
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar una categoría correctamente")
    public void testGuardarCategoria() {
        // GIVEN
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // WHEN
        CategoriaDTO resultado = categoriaService.guardarCategoria(categoriaDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(categoria.getNombre(), resultado.getNombre());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    // ─────────────────────────────────────────────
    // actualizarCategoria
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar una categoría existente correctamente")
    public void testActualizarCategoria() {
        // GIVEN
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // WHEN
        CategoriaDTO resultado = categoriaService.actualizarCategoria(1, categoriaDTO);

        // THEN
        assertNotNull(resultado);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Debe retornar null al actualizar si la categoría no existe")
    public void testActualizarCategoriaNoExiste() {
        // GIVEN
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        CategoriaDTO resultado = categoriaService.actualizarCategoria(99, categoriaDTO);

        // THEN
        assertNull(resultado);
        verify(categoriaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // eliminarCategoria
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar una categoría existente y retornar true")
    public void testEliminarCategoria() {
        // GIVEN
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        doNothing().when(categoriaRepository).delete(categoria);

        // WHEN
        boolean resultado = categoriaService.eliminarCategoria(1);

        // THEN
        assertTrue(resultado);
        verify(categoriaRepository, times(1)).delete(categoria);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar si la categoría no existe")
    public void testEliminarCategoriaNoExiste() {
        // GIVEN
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = categoriaService.eliminarCategoria(99);

        // THEN
        assertFalse(resultado);
        verify(categoriaRepository, never()).delete(any());
    }
}