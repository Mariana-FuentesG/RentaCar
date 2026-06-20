package com.prueba.ms_reservas;

import com.prueba.ms_reservas.client.ClienteClient;
import com.prueba.ms_reservas.client.VehiculoClient;
import com.prueba.ms_reservas.dto.ClienteDTO;
import com.prueba.ms_reservas.dto.ReservaDTO;
import com.prueba.ms_reservas.dto.VehiculoDTO;
import com.prueba.ms_reservas.model.EstadoReserva;
import com.prueba.ms_reservas.model.Reserva;
import com.prueba.ms_reservas.repository.EstadoReservaRepository;
import com.prueba.ms_reservas.repository.ReservaRepository;
import com.prueba.ms_reservas.service.ReservaService;
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
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private EstadoReservaRepository estadoReservaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private VehiculoClient vehiculoClient;

    @InjectMocks
    private ReservaService reservaService;

    private final Faker faker = new Faker();

    private EstadoReserva estadoReserva;
    private Reserva reserva;
    private ReservaDTO reservaDTO;
    private ClienteDTO clienteDTO;
    private VehiculoDTO vehiculoDTO;

    @BeforeEach
    void setUp() {
        // GIVEN → datos base reutilizables
        estadoReserva = new EstadoReserva();
        estadoReserva.setId(1);
        estadoReserva.setNombreEstado("Pendiente");
        estadoReserva.setPrioridad(1);
        estadoReserva.setActivo(true);
        estadoReserva.setFechaCreacion(LocalDate.now());
        estadoReserva.setDiasLimitePago(3);

        reserva = new Reserva();
        reserva.setId(1);
        reserva.setClienteId(1);
        reserva.setVehiculoId(1);
        reserva.setMontoReserva(faker.number().randomDouble(2, 50000, 500000));
        reserva.setCantidadDias(faker.number().numberBetween(1, 15));
        reserva.setPagada(false);
        reserva.setFechaInicio(LocalDate.now());
        reserva.setFechaTermino(LocalDate.now().plusDays(3));
        reserva.setFechaReserva(LocalDate.now());
        reserva.setObservacion(faker.lorem().sentence());
        reserva.setEstadoReserva(estadoReserva);

        reservaDTO = new ReservaDTO();
        reservaDTO.setClienteId(1);
        reservaDTO.setVehiculoId(1);
        reservaDTO.setMontoReserva(reserva.getMontoReserva());
        reservaDTO.setCantidadDias(reserva.getCantidadDias());
        reservaDTO.setPagada(false);
        reservaDTO.setFechaInicio(LocalDate.now());
        reservaDTO.setFechaTermino(LocalDate.now().plusDays(3));
        reservaDTO.setFechaReserva(LocalDate.now());
        reservaDTO.setObservacion(faker.lorem().sentence());
        reservaDTO.setEstadoReservaId(1);

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1);
        clienteDTO.setNombreCompleto(faker.name().fullName());

        vehiculoDTO = new VehiculoDTO();
        vehiculoDTO.setId(1);
        vehiculoDTO.setMarca(faker.company().name());
        vehiculoDTO.setModelo(faker.lorem().word());
    }

    // ─────────────────────────────────────────────
    // obtenerReservas
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista de reservas con datos de cliente y vehículo")
    void obtenerReservas_retornaLista() {
        // GIVEN
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);

        // WHEN
        List<ReservaDTO> resultado = reservaService.obtenerReservas();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(clienteDTO.getNombreCompleto(), resultado.get(0).getNombreCliente());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay reservas")
    void obtenerReservas_listaVacia() {
        // GIVEN
        when(reservaRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<ReservaDTO> resultado = reservaService.obtenerReservas();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerReservaPorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar una reserva existente por ID")
    void obtenerReservaPorId_existente() {
        // GIVEN
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);

        // WHEN
        ReservaDTO resultado = reservaService.obtenerReservaPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(clienteDTO.getNombreCompleto(), resultado.getNombreCliente());
    }

    @Test
    @DisplayName("Debe retornar null si la reserva no existe")
    void obtenerReservaPorId_noExiste() {
        // GIVEN
        when(reservaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        ReservaDTO resultado = reservaService.obtenerReservaPorId(99);

        // THEN
        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // guardarReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar una reserva correctamente")
    void guardarReserva_exitoso() {
        // GIVEN
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estadoReserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // WHEN
        ReservaDTO resultado = reservaService.guardarReserva(reservaDTO);

        // THEN
        assertNotNull(resultado);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe retornar null si el cliente no existe")
    void guardarReserva_clienteNoExiste() {
        // GIVEN
        when(clienteClient.obtenerClientePorId(1)).thenReturn(null);

        // WHEN
        ReservaDTO resultado = reservaService.guardarReserva(reservaDTO);

        // THEN
        assertNull(resultado);
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar null si el vehículo no existe")
    void guardarReserva_vehiculoNoExiste() {
        // GIVEN
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(null);

        // WHEN
        ReservaDTO resultado = reservaService.guardarReserva(reservaDTO);

        // THEN
        assertNull(resultado);
        verify(reservaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe retornar null si el estado de reserva no existe")
    void guardarReserva_estadoNoExiste() {
        // GIVEN
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.empty());

        // WHEN
        ReservaDTO resultado = reservaService.guardarReserva(reservaDTO);

        // THEN
        assertNull(resultado);
        verify(reservaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // actualizarReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar una reserva existente correctamente")
    void actualizarReserva_exitoso() {
        // GIVEN
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estadoReserva));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        // WHEN
        ReservaDTO resultado = reservaService.actualizarReserva(1, reservaDTO);

        // THEN
        assertNotNull(resultado);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe retornar null al actualizar si la reserva no existe")
    void actualizarReserva_noExiste() {
        // GIVEN
        when(reservaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        ReservaDTO resultado = reservaService.actualizarReserva(99, reservaDTO);

        // THEN
        assertNull(resultado);
        verify(reservaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // eliminarReserva
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar una reserva existente y retornar true")
    void eliminarReserva_exitoso() {
        // GIVEN
        when(reservaRepository.findById(1)).thenReturn(Optional.of(reserva));
        doNothing().when(reservaRepository).delete(reserva);

        // WHEN
        boolean resultado = reservaService.eliminarReserva(1);

        // THEN
        assertTrue(resultado);
        verify(reservaRepository, times(1)).delete(reserva);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar si la reserva no existe")
    void eliminarReserva_noExiste() {
        // GIVEN
        when(reservaRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = reservaService.eliminarReserva(99);

        // THEN
        assertFalse(resultado);
        verify(reservaRepository, never()).delete(any());
    }

    // ─────────────────────────────────────────────
    // buscarReservasDesdeFecha
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar reservas desde una fecha dada")
    void buscarReservasDesdeFecha_retornaLista() {
        // GIVEN
        LocalDate fecha = LocalDate.now().minusDays(7);
        when(reservaRepository.buscarReservasDesdeFecha(fecha)).thenReturn(List.of(reserva));
        when(clienteClient.obtenerClientePorId(1)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculoPorId(1)).thenReturn(vehiculoDTO);

        // WHEN
        List<ReservaDTO> resultado = reservaService.buscarReservasDesdeFecha(fecha);

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(reservaRepository, times(1)).buscarReservasDesdeFecha(fecha);
    }
}