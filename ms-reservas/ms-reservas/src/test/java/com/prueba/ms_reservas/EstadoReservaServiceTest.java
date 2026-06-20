package com.prueba.ms_reservas;

import com.prueba.ms_reservas.dto.EstadoReservaDTO;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.repository.EstadoReservaRepository;
import com.prueba.ms_reservas.service.EstadoReservaService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoReservaServiceTest {

    @Mock
    private EstadoReservaRepository estadoReservaRepository;

    @InjectMocks
    private EstadoReservaService estadoReservaService;

    private final Faker faker = new Faker();

    private EstadoReserva estadoReserva;
    private EstadoReservaDTO estadoReservaDTO;

    @BeforeEach
    void setUp() {
        // GIVEN → datos base reutilizables
        estadoReserva = new EstadoReserva();
        estadoReserva.setId(1);
        estadoReserva.setNombreEstado(faker.lorem().word());
        estadoReserva.setPrioridad(faker.number().numberBetween(1, 5));
        estadoReserva.setActivo(true);
        estadoReserva.setFechaCreacion(LocalDate.now());
        estadoReserva.setDiasLimitePago(faker.number().numberBetween(1, 10));

        estadoReservaDTO = new EstadoReservaDTO();
        estadoReservaDTO.setId(1);
        estadoReservaDTO.setNombreEstado(estadoReserva.getNombreEstado());
        estadoReservaDTO.setPrioridad(estadoReserva.getPrioridad());
        estadoReservaDTO.setActivo(true);
        estadoReservaDTO.setFechaCreacion(LocalDate.now());
        estadoReservaDTO.setDiasLimitePago(estadoReserva.getDiasLimitePago());
    }

    // ─────────────────────────────────────────────
    // obtenerEstadosReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista con todos los estados de reserva")
    void obtenerEstadosReserva_retornaLista() {
        // GIVEN
        when(estadoReservaRepository.findAll()).thenReturn(List.of(estadoReserva));

        // WHEN
        List<EstadoReservaDTO> resultado = estadoReservaService.obtenerEstadosReserva();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(estadoReserva.getNombreEstado(), resultado.get(0).getNombreEstado());
        verify(estadoReservaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay estados")
    void obtenerEstadosReserva_listaVacia() {
        // GIVEN
        when(estadoReservaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<EstadoReservaDTO> resultado = estadoReservaService.obtenerEstadosReserva();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerEstadoReservaPorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar un estado de reserva existente por ID")
    void obtenerEstadoReservaPorId_existente() {
        // GIVEN
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estadoReserva));

        // WHEN
        EstadoReservaDTO resultado = estadoReservaService.obtenerEstadoReservaPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(estadoReserva.getNombreEstado(), resultado.getNombreEstado());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el estado no existe")
    void obtenerEstadoReservaPorId_noExiste() {
        // GIVEN
        when(estadoReservaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN - THEN
        assertThrows(RuntimeException.class,
                () -> estadoReservaService.obtenerEstadoReservaPorId(99));
    }

    // ─────────────────────────────────────────────
    // guardarEstadoReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar un estado de reserva correctamente")
    void guardarEstadoReserva_exitoso() {
        // GIVEN
        when(estadoReservaRepository.save(any(EstadoReserva.class))).thenReturn(estadoReserva);

        // WHEN
        EstadoReservaDTO resultado = estadoReservaService.guardarEstadoReserva(estadoReservaDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(estadoReserva.getNombreEstado(), resultado.getNombreEstado());
        verify(estadoReservaRepository, times(1)).save(any(EstadoReserva.class));
    }

    // ─────────────────────────────────────────────
    // actualizarEstadoReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar un estado de reserva existente")
    void actualizarEstadoReserva_exitoso() {
        // GIVEN
        EstadoReservaDTO dtoActualizado = new EstadoReservaDTO();
        dtoActualizado.setNombreEstado("Confirmada");
        dtoActualizado.setPrioridad(2);
        dtoActualizado.setActivo(true);
        dtoActualizado.setFechaCreacion(LocalDate.now());
        dtoActualizado.setDiasLimitePago(5);

        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estadoReserva));
        when(estadoReservaRepository.save(any(EstadoReserva.class))).thenReturn(estadoReserva);

        // WHEN
        EstadoReservaDTO resultado = estadoReservaService.actualizarEstadoReserva(1, dtoActualizado);

        // THEN
        assertNotNull(resultado);
        verify(estadoReservaRepository, times(1)).save(any(EstadoReserva.class));
    }

    @Test
    @DisplayName("Debe retornar null al actualizar si el estado no existe")
    void actualizarEstadoReserva_noExiste() {
        // GIVEN
        when(estadoReservaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        EstadoReservaDTO resultado = estadoReservaService.actualizarEstadoReserva(99, estadoReservaDTO);

        // THEN
        assertNull(resultado);
        verify(estadoReservaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // eliminarEstadoReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar un estado existente y retornar true")
    void eliminarEstadoReserva_exitoso() {
        // GIVEN
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estadoReserva));
        doNothing().when(estadoReservaRepository).delete(estadoReserva);

        // WHEN
        boolean resultado = estadoReservaService.eliminarEstadoReserva(1);

        // THEN
        assertTrue(resultado);
        verify(estadoReservaRepository, times(1)).delete(estadoReserva);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar si el estado no existe")
    void eliminarEstadoReserva_noExiste() {
        // GIVEN
        when(estadoReservaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = estadoReservaService.eliminarEstadoReserva(99);

        // THEN
        assertFalse(resultado);
        verify(estadoReservaRepository, never()).delete(any());
    }
}