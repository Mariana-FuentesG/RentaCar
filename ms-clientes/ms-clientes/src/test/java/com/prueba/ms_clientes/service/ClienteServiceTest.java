package com.prueba.ms_clientes.service;

import com.prueba.ms_clientes.dto.ClienteDTO;
import com.prueba.ms_clientes.model.Cliente;
import com.prueba.ms_clientes.repository.ClienteRepository;
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
public class ClienteServiceTest {

    // Inyecta el servicio real de Cliente para ser probado
    @Autowired
    private ClienteService clienteService;

    // Crea mock del repositorio para simular su comportamiento
    @MockitoBean
    private ClienteRepository clienteRepository;

    private final Faker faker = new Faker();

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    // Se ejecuta UNA VEZ antes de cada test
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setRut(faker.number().numberBetween(10000000, 25000000));
        cliente.setNombreCompleto(faker.name().fullName());
        cliente.setEmail(faker.internet().emailAddress());
        cliente.setTelefono(faker.number().digits(9));
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDate.now());

        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1);
        clienteDTO.setRut(cliente.getRut());
        clienteDTO.setNombreCompleto(cliente.getNombreCompleto());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setActivo(true);
        clienteDTO.setFechaRegistro(LocalDate.now());
    }

    // ─────────────────────────────────────────────
    // obtenerClientes
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista con todos los clientes")
    public void testObtenerClientes() {
        // GIVEN
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        // WHEN
        List<ClienteDTO> resultado = clienteService.obtenerClientes();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay clientes")
    public void testObtenerClientesVacio() {
        // GIVEN
        when(clienteRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<ClienteDTO> resultado = clienteService.obtenerClientes();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerClientePorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar un cliente existente por ID")
    public void testObtenerClientePorId() {
        // GIVEN
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));

        // WHEN
        ClienteDTO resultado = clienteService.obtenerClientePorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(cliente.getNombreCompleto(), resultado.getNombreCompleto());
        verify(clienteRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe retornar null si el cliente no existe")
    public void testObtenerClientePorIdNoExiste() {
        // GIVEN
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        ClienteDTO resultado = clienteService.obtenerClientePorId(99);

        // THEN
        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // obtenerClientesPorEmail
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar clientes que coincidan con el email")
    public void testObtenerClientesPorEmail() {
        // GIVEN
        when(clienteRepository.findByEmailContainingIgnoreCase(cliente.getEmail()))
                .thenReturn(List.of(cliente));

        // WHEN
        List<ClienteDTO> resultado = clienteService.obtenerClientesPorEmail(cliente.getEmail());

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(clienteRepository, times(1))
                .findByEmailContainingIgnoreCase(cliente.getEmail());
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay clientes con ese email")
    public void testObtenerClientesPorEmailVacio() {
        // GIVEN
        when(clienteRepository.findByEmailContainingIgnoreCase("noexiste@mail.com"))
                .thenReturn(List.of());

        // WHEN
        List<ClienteDTO> resultado = clienteService.obtenerClientesPorEmail("noexiste@mail.com");

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // guardarCliente
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar un cliente correctamente")
    public void testGuardarCliente() {
        // GIVEN
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // WHEN
        ClienteDTO resultado = clienteService.guardarCliente(clienteDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(cliente.getNombreCompleto(), resultado.getNombreCompleto());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    // ─────────────────────────────────────────────
    // actualizarCliente
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar un cliente existente correctamente")
    public void testActualizarCliente() {
        // GIVEN
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // WHEN
        ClienteDTO resultado = clienteService.actualizarCliente(1, clienteDTO);

        // THEN
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe retornar null al actualizar si el cliente no existe")
    public void testActualizarClienteNoExiste() {
        // GIVEN
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        ClienteDTO resultado = clienteService.actualizarCliente(99, clienteDTO);

        // THEN
        assertNull(resultado);
        verify(clienteRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // eliminarClienteId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar un cliente existente y retornar true")
    public void testEliminarCliente() {
        // GIVEN
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        // WHEN
        boolean resultado = clienteService.eliminarClienteId(1);

        // THEN
        assertTrue(resultado);
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar si el cliente no existe")
    public void testEliminarClienteNoExiste() {
        // GIVEN
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = clienteService.eliminarClienteId(99);

        // THEN
        assertFalse(resultado);
        verify(clienteRepository, never()).delete(any());
    }

    // ─────────────────────────────────────────────
    // obtenerClientesActivos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista de clientes activos")
    public void testObtenerClientesActivos() {
        // GIVEN
        when(clienteRepository.obtenerClientesActivos()).thenReturn(List.of(cliente));

        // WHEN
        List<ClienteDTO> resultado = clienteService.obtenerClientesActivos();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(clienteRepository, times(1)).obtenerClientesActivos();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay clientes activos")
    public void testObtenerClientesActivosVacio() {
        // GIVEN
        when(clienteRepository.obtenerClientesActivos()).thenReturn(List.of());

        // WHEN
        List<ClienteDTO> resultado = clienteService.obtenerClientesActivos();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}