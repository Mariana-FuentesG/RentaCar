package com.prueba.ms_vehiculos.service;

import com.prueba.ms_vehiculos.dto.VehiculoDTO;
import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.model.Vehiculo;
import com.prueba.ms_vehiculos.repository.CategoriaRepository;
import com.prueba.ms_vehiculos.repository.VehiculoRepository;
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
public class VehiculoServiceTest {

    @Autowired
    private VehiculoService vehiculoService;

    @MockitoBean
    private VehiculoRepository vehiculoRepository;

    @MockitoBean
    private CategoriaRepository categoriaRepository;

    private final Faker faker = new Faker();

    private Vehiculo vehiculo;
    private VehiculoDTO vehiculoDTO;
    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        // GIVEN → datos base reutilizables en todos los tests
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre(faker.options().option("SUV", "Sedan", "Hatchback", "Pickup"));
        categoria.setCapacidadPasajeros(5);
        categoria.setActiva(true);
        categoria.setFechaCreacion(LocalDate.now());
        categoria.setPrecioBase(faker.number().randomDouble(2, 50000, 200000));

        vehiculo = new Vehiculo();
        vehiculo.setId(1);
        vehiculo.setPatente(faker.bothify("????##").toUpperCase());
        vehiculo.setMarca(faker.options().option("Toyota", "Chevrolet", "Hyundai", "Kia"));
        vehiculo.setModelo(faker.options().option("Corolla", "Spark", "Tucson", "Sportage"));
        vehiculo.setPrecioDiario(faker.number().randomDouble(2, 30000, 150000));
        vehiculo.setAnio(faker.number().numberBetween(2015, 2026));
        vehiculo.setDisponible(true);
        vehiculo.setFechaIngreso(LocalDate.now());
        vehiculo.setCategoria(categoria);

        vehiculoDTO = VehiculoDTO.builder()
                .id(1)
                .patente(vehiculo.getPatente())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .precioDiario(vehiculo.getPrecioDiario())
                .anio(vehiculo.getAnio())
                .disponible(true)
                .fechaIngreso(LocalDate.now())
                .categoriaId(1)
                .build();
    }

    // ─────────────────────────────────────────────
    // obtenerVehiculos
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista con todos los vehículos")
    public void testObtenerVehiculos() {
        // GIVEN
        when(vehiculoRepository.findAll()).thenReturn(List.of(vehiculo));

        // WHEN
        List<VehiculoDTO> resultado = vehiculoService.obtenerVehiculos();

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(vehiculoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay vehículos")
    public void testObtenerVehiculosVacio() {
        // GIVEN
        when(vehiculoRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<VehiculoDTO> resultado = vehiculoService.obtenerVehiculos();

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────
    // obtenerVehiculoPorId
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar un vehículo existente por ID")
    public void testObtenerVehiculoPorId() {
        // GIVEN
        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));

        // WHEN
        VehiculoDTO resultado = vehiculoService.obtenerVehiculoPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(vehiculo.getMarca(), resultado.getMarca());
        verify(vehiculoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe retornar null si el vehículo no existe")
    public void testObtenerVehiculoPorIdNoExiste() {
        // GIVEN
        when(vehiculoRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        VehiculoDTO resultado = vehiculoService.obtenerVehiculoPorId(99);

        // THEN
        assertNull(resultado);
    }

    // ─────────────────────────────────────────────
    // guardarVehiculo
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe guardar un vehículo correctamente")
    public void testGuardarVehiculo() {
        // GIVEN
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // WHEN
        VehiculoDTO resultado = vehiculoService.guardarVehiculo(vehiculoDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(vehiculo.getMarca(), resultado.getMarca());
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    // ─────────────────────────────────────────────
    // actualizarVehiculo
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe actualizar un vehículo existente correctamente")
    public void testActualizarVehiculo() {
        // GIVEN
        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);

        // WHEN
        VehiculoDTO resultado = vehiculoService.actualizarVehiculo(1, vehiculoDTO);

        // THEN
        assertNotNull(resultado);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Debe retornar null al actualizar si el vehículo no existe")
    public void testActualizarVehiculoNoExiste() {
        // GIVEN
        when(vehiculoRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        VehiculoDTO resultado = vehiculoService.actualizarVehiculo(99, vehiculoDTO);

        // THEN
        assertNull(resultado);
        verify(vehiculoRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────
    // eliminarVehiculo
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe eliminar un vehículo existente y retornar true")
    public void testEliminarVehiculo() {
        // GIVEN
        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));
        doNothing().when(vehiculoRepository).delete(vehiculo);

        // WHEN
        boolean resultado = vehiculoService.eliminarVehiculo(1);

        // THEN
        assertTrue(resultado);
        verify(vehiculoRepository, times(1)).delete(vehiculo);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar si el vehículo no existe")
    public void testEliminarVehiculoNoExiste() {
        // GIVEN
        when(vehiculoRepository.findById(99)).thenReturn(Optional.empty());

        // WHEN
        boolean resultado = vehiculoService.eliminarVehiculo(99);

        // THEN
        assertFalse(resultado);
        verify(vehiculoRepository, never()).delete(any());
    }

    // ─────────────────────────────────────────────
    // obtenerVehiculosDisponiblesPorPrecio
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar vehículos disponibles con precio menor al indicado")
    public void testObtenerVehiculosDisponiblesPorPrecio() {
        // GIVEN
        when(vehiculoRepository.findByDisponibleTrueAndPrecioDiarioLessThan(100000.0))
                .thenReturn(List.of(vehiculo));

        // WHEN
        List<VehiculoDTO> resultado = vehiculoService
                .obtenerVehiculosDisponiblesPorPrecio(100000.0);

        // THEN
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(vehiculoRepository, times(1))
                .findByDisponibleTrueAndPrecioDiarioLessThan(100000.0);
    }

    @Test
    @DisplayName("Debe retornar lista vacía si no hay vehículos en el rango de precio")
    public void testObtenerVehiculosDisponiblesPorPrecioVacio() {
        // GIVEN
        when(vehiculoRepository.findByDisponibleTrueAndPrecioDiarioLessThan(1000.0))
                .thenReturn(List.of());

        // WHEN
        List<VehiculoDTO> resultado = vehiculoService
                .obtenerVehiculosDisponiblesPorPrecio(1000.0);

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}