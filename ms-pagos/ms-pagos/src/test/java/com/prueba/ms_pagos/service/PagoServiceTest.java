package com.prueba.ms_pagos.service;

import com.prueba.ms_pagos.client.ReservaClient;
import com.prueba.ms_pagos.dto.PagoDTO;
import com.prueba.ms_pagos.dto.ReservaDTO;
import com.prueba.ms_pagos.model.Pago;
import com.prueba.ms_pagos.repository.PagoRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PagoServiceTest {

    // Inyecta el servicio real de Pago para ser probado
    @Autowired
    private PagoService pagoService;

    // Crea mocks para simular el comportamiento del repositorio y cliente Feign
    @MockitoBean
    private PagoRepository pagoRepository;

    @MockitoBean
    private ReservaClient reservaClient;

    private final Faker faker = new Faker();

    private Pago pago;
    private PagoDTO pagoDTO;
    private ReservaDTO reservaDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        pago = Pago.builder()
                .id(1)
                .reservaId(1)
                .monto(faker.number().randomDouble(2, 50000, 500000))
                .pagado(true)
                .fechaPago(LocalDate.now())
                .metodoPago(faker.options().option(
                        "Tarjeta de Crédito",
                        "Transferencia Bancaria",
                        "Efectivo"
                ))
                .cantCuotas(faker.number().numberBetween(1, 12))
                .build();

        pagoDTO = PagoDTO.builder()
                .id(1)
                .reservaId(1)
                .monto(pago.getMonto())
                .pagado(true)
                .fechaPago(LocalDate.now())
                .metodoPago(pago.getMetodoPago())
                .cantCuotas(pago.getCantCuotas())
                .build();

        reservaDTO = new ReservaDTO();
        reservaDTO.setId(1);
        reservaDTO.setClienteId(1);
        reservaDTO.setVehiculoId(1);
        reservaDTO.setMontoReserva(pago.getMonto());
        reservaDTO.setCantidadDias(faker.number().numberBetween(1, 15));
        reservaDTO.setPagada(true);
        reservaDTO.setFechaInicio(LocalDate.now().plusDays(1));
        reservaDTO.setFechaTermino(LocalDate.now().plusDays(4));
        reservaDTO.setFechaReserva(LocalDate.now());
        reservaDTO.setEstadoReservaId(1);
        reservaDTO.setNombreEstado("Confirmada");
    }

    // ─────────────────────────────────────────────
    // obtenerPagos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar la lista completa de pagos")
    public void testObtenerPagos() {
        // GIVEN
        Pago otroPago = Pago.builder()
                .id(2).reservaId(2).monto(200000.0).pagado(false)
                .fechaPago(LocalDate.now())
                .metodoPago("Transferencia Bancaria").cantCuotas(2)
                .build();
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago, otroPago));

        // WHEN
        List<PagoDTO> resultado = pagoService.obtenerPagos();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no existen pagos")
    public void testObtenerPagosVacio() {
        // GIVEN
        when(pagoRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<PagoDTO> resultado = pagoService.obtenerPagos();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerPagoPorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar el pago con datos de la reserva cuando existe")
    public void testObtenerPagoPorId() {
        // GIVEN
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(reservaClient.obtenerReservaPorId(1)).thenReturn(reservaDTO);

        // WHEN
        PagoDTO resultado = pagoService.obtenerPagoPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertNotNull(resultado.getMontoReserva());
        verify(pagoRepository, times(1)).findById(1);
        verify(reservaClient, times(1)).obtenerReservaPorId(1);
    }

    @Test
    @DisplayName("Debe retornar null cuando el pago no existe")
    public void testObtenerPagoPorIdNoExiste() {
        // GIVEN
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        PagoDTO resultado = pagoService.obtenerPagoPorId(99);

        // THEN
        assertNull(resultado);
        verify(reservaClient, never()).obtenerReservaPorId(any());
    }

    @Test
    @DisplayName("Debe retornar el pago sin datos de reserva cuando ms-reservas no responde")
    public void testObtenerPagoPorIdFeignFalla() {
        // GIVEN
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(reservaClient.obtenerReservaPorId(1)).thenReturn(null);

        // WHEN
        PagoDTO resultado = pagoService.obtenerPagoPorId(1);

        // THEN
        assertNotNull(resultado);
        assertNull(resultado.getMontoReserva());
        assertNull(resultado.getReservaPagada());
    }

    // ─────────────────────────────────────────────
    // guardarPago
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar el pago cuando la reserva existe")
    public void testGuardarPago() {
        // GIVEN
        when(reservaClient.obtenerReservaPorId(1)).thenReturn(reservaDTO);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        PagoDTO resultado = pagoService.guardarPago(pagoDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(reservaClient, times(1)).obtenerReservaPorId(1);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe retornar null cuando la reserva asociada no existe")
    public void testGuardarPagoReservaNoExiste() {
        // GIVEN
        when(reservaClient.obtenerReservaPorId(1)).thenReturn(null);

        // WHEN
        PagoDTO resultado = pagoService.guardarPago(pagoDTO);

        // THEN
        assertNull(resultado);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    // ─────────────────────────────────────────────
    // actualizarPago
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar el pago cuando existe y la reserva es válida")
    public void testActualizarPago() {
        // GIVEN
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(reservaClient.obtenerReservaPorId(1)).thenReturn(reservaDTO);
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        PagoDTO resultado = pagoService.actualizarPago(1, pagoDTO);

        // THEN
        assertNotNull(resultado);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe retornar null cuando el pago no existe")
    public void testActualizarPagoNoExiste() {
        // GIVEN
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        PagoDTO resultado = pagoService.actualizarPago(99, pagoDTO);

        // THEN
        assertNull(resultado);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    @DisplayName("Debe retornar null cuando la reserva asociada no existe")
    public void testActualizarPagoReservaNoExiste() {
        // GIVEN
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));
        when(reservaClient.obtenerReservaPorId(1)).thenReturn(null);

        // WHEN
        PagoDTO resultado = pagoService.actualizarPago(1, pagoDTO);

        // THEN
        assertNull(resultado);
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    // ─────────────────────────────────────────────
    // eliminarPago
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar true cuando el pago existe y se elimina correctamente")
    public void testEliminarPago() {
        // GIVEN
        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        // WHEN
        boolean resultado = pagoService.eliminarPago(1);

        // THEN
        assertTrue(resultado);
        verify(pagoRepository, times(1)).delete(pago);
    }

    @Test
    @DisplayName("Debe retornar false cuando el pago no existe")
    public void testEliminarPagoNoExiste() {
        // GIVEN
        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = pagoService.eliminarPago(99);

        // THEN
        assertFalse(resultado);
        verify(pagoRepository, never()).delete(any(Pago.class));
    }

    // ─────────────────────────────────────────────
    // buscarPagosPorMonto
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar los pagos dentro del rango solicitado")
    public void testBuscarPagosPorMonto() {
        // GIVEN
        when(pagoRepository.buscarPagosPorMonto(anyDouble(), anyDouble()))
                .thenReturn(List.of(pago));

        // WHEN
        List<PagoDTO> resultado = pagoService.buscarPagosPorMonto(50000.0, 200000.0);

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(pagoRepository, times(1)).buscarPagosPorMonto(50000.0, 200000.0);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando ningún pago está en el rango")
    public void testBuscarPagosPorMontoVacio() {
        // GIVEN
        when(pagoRepository.buscarPagosPorMonto(anyDouble(), anyDouble()))
                .thenReturn(List.of());

        // WHEN
        List<PagoDTO> resultado = pagoService.buscarPagosPorMonto(1000.0, 2000.0);

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}