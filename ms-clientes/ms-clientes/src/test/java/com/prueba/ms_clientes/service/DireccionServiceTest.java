package com.prueba.ms_clientes.service;

import com.prueba.ms_clientes.dto.DireccionDTO;
import com.prueba.ms_clientes.model.Direccion;
import com.prueba.ms_clientes.repository.DireccionRepository;
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
public class DireccionServiceTest {

    // Inyecta el servicio real de Direccion para ser probado
    @Autowired
    private DireccionService direccionService;

    // Crea mock del repositorio para simular su comportamiento
    @MockitoBean
    private DireccionRepository direccionRepository;

    private final Faker faker = new Faker();

    private Direccion direccion;
    private DireccionDTO direccionDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        direccion = new Direccion();
        direccion.setId(1);
        direccion.setCalle(faker.address().streetName());
        direccion.setNumeroCasa(faker.number().numberBetween(1, 9999));
        direccion.setComuna(faker.options().option(
                "Providencia", "Las Condes", "Santiago", "Ñuñoa"
        ));
        direccion.setCiudad("Santiago");
        direccion.setCodigoPostal(faker.number().numberBetween(1000000, 9999999));
        direccion.setEstado(true);
        direccion.setFechaRegistro(LocalDate.now());

        direccionDTO = DireccionDTO.builder()
                .id(1)
                .calle(direccion.getCalle())
                .numeroCasa(direccion.getNumeroCasa())
                .comuna(direccion.getComuna())
                .ciudad(direccion.getCiudad())
                .codigoPostal(direccion.getCodigoPostal())
                .estado(true)
                .fechaRegistro(LocalDate.now())
                .clienteId(1)
                .build();
    }

    // ─────────────────────────────────────────────
    // obtenerDirecciones
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista con todas las direcciones")
    public void testObtenerDirecciones() {
        // GIVEN
        when(direccionRepository.findAll()).thenReturn(List.of(direccion));

        // WHEN
        List<DireccionDTO> resultado = direccionService.obtenerDirecciones();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(direccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay direcciones")
    public void testObtenerDireccionesVacio() {
        // GIVEN
        when(direccionRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<DireccionDTO> resultado = direccionService.obtenerDirecciones();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerDireccionPorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar una dirección existente por ID")
    public void testObtenerDireccionPorId() {
        // GIVEN
        when(direccionRepository.findById(1)).thenReturn(Optional.of(direccion));

        // WHEN
        DireccionDTO resultado = direccionService.obtenerDireccionPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(direccionRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe retornar null si la dirección no existe")
    public void testObtenerDireccionPorIdNoExiste() {
        // GIVEN
        when(direccionRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        DireccionDTO resultado = direccionService.obtenerDireccionPorId(99);

        // THEN
        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // guardarDireccion
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar una dirección correctamente")
    public void testGuardarDireccion() {
        // GIVEN
        when(direccionRepository.save(any(Direccion.class))).thenReturn(direccion);

        // WHEN
        DireccionDTO resultado = direccionService.guardarDireccion(direccionDTO);

        // THEN
        assertNotNull(resultado);
        verify(direccionRepository, times(1)).save(any(Direccion.class));
    }

    // ─────────────────────────────────────────────
    // actualizarDireccion
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar una dirección existente correctamente")
    public void testActualizarDireccion() {
        // GIVEN
        when(direccionRepository.findById(1)).thenReturn(Optional.of(direccion));
        when(direccionRepository.save(any(Direccion.class))).thenReturn(direccion);

        // WHEN
        DireccionDTO resultado = direccionService.actualizarDireccion(1, direccionDTO);

        // THEN
        assertNotNull(resultado);
        verify(direccionRepository, times(1)).save(any(Direccion.class));
    }

    @Test
    @DisplayName("Debe retornar null al actualizar si la dirección no existe")
    public void testActualizarDireccionNoExiste() {
        // GIVEN
        when(direccionRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        DireccionDTO resultado = direccionService.actualizarDireccion(99, direccionDTO);

        // THEN
        assertNull(resultado);
        verify(direccionRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // buscarPorComuna
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar direcciones que coincidan con la comuna")
    public void testBuscarPorComuna() {
        // GIVEN
        when(direccionRepository.findByComunaContainingIgnoreCase("Santiago"))
                .thenReturn(List.of(direccion));

        // WHEN
        List<DireccionDTO> resultado = direccionService.buscarPorComuna("Santiago");

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(direccionRepository, times(1))
                .findByComunaContainingIgnoreCase("Santiago");
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay direcciones en esa comuna")
    public void testBuscarPorComunaVacio() {
        // GIVEN
        when(direccionRepository.findByComunaContainingIgnoreCase("NoExiste"))
                .thenReturn(List.of());

        // WHEN
        List<DireccionDTO> resultado = direccionService.buscarPorComuna("NoExiste");

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // eliminarDireccion
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar una dirección existente y retornar true")
    public void testEliminarDireccion() {
        // GIVEN
        when(direccionRepository.findById(1)).thenReturn(Optional.of(direccion));
        doNothing().when(direccionRepository).delete(direccion);

        // WHEN
        boolean resultado = direccionService.eliminarDireccion(1);

        // THEN
        assertTrue(resultado);
        verify(direccionRepository, times(1)).delete(direccion);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar si la dirección no existe")
    public void testEliminarDireccionNoExiste() {
        // GIVEN
        when(direccionRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = direccionService.eliminarDireccion(99);

        // THEN
        assertFalse(resultado);
        verify(direccionRepository, never()).delete(any());
    }
}