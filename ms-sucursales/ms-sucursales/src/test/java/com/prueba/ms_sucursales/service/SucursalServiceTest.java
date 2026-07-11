package com.prueba.ms_sucursales.service;

import com.prueba.ms_sucursales.dto.request.SucursalRequestDTO;
import com.prueba.ms_sucursales.exception.ResourceNotFoundException;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.repository.RegionRepository;
import com.prueba.ms_sucursales.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Region region;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setId(1);
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setActiva(true);

        sucursal = new Sucursal();
        sucursal.setId(1);
        sucursal.setNombre("Sucursal Santiago");
        sucursal.setDireccion("Alameda 123");
        sucursal.setTelefono("987654321");
        sucursal.setCiudad("Santiago");
        sucursal.setActiva(true);
        sucursal.setCantidadVehiculos(20);
        sucursal.setRegion(region);
    }

    @Test
    void debeRetornarListaDeSucursales() {
        when(sucursalRepository.findAll()).thenReturn(List.of(sucursal));

        List<Sucursal> resultado = sucursalService.obtenerSucursales();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Sucursal Santiago");
    }

    @Test
    void debeRetornarSucursalPorId() {
        when(sucursalRepository.findById(1)).thenReturn(Optional.of(sucursal));

        Sucursal resultado = sucursalService.obtenerSucursalPorId(1);

        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getRegion().getId()).isEqualTo(1);
    }

    @Test
    void debeLanzarExcepcionSiSucursalNoExiste() {
        when(sucursalRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sucursalService.obtenerSucursalPorId(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void debeCrearUnaSucursal() {
        SucursalRequestDTO dto = new SucursalRequestDTO(
                "Sucursal Nueva", "Calle 1", "911111111", "Santiago", true, 5, 1);

        when(regionRepository.findById(1)).thenReturn(Optional.of(region));
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);

        Sucursal resultado = sucursalService.guardarSucursal(dto);

        assertThat(resultado).isNotNull();
        verify(sucursalRepository, times(1)).save(any(Sucursal.class));
    }

    @Test
    void debeLanzarExcepcionSiRegionNoExisteAlCrear() {
        SucursalRequestDTO dto = new SucursalRequestDTO(
                "Sucursal Nueva", "Calle 1", "911111111", "Santiago", true, 5, 99);

        when(regionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sucursalService.guardarSucursal(dto))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void debeEliminarUnaSucursalExistente() {
        when(sucursalRepository.existsById(1)).thenReturn(true);

        sucursalService.eliminarSucursal(1);

        verify(sucursalRepository, times(1)).deleteById(1);
    }

    @Test
    void debeLanzarExcepcionAlEliminarSucursalInexistente() {
        when(sucursalRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> sucursalService.eliminarSucursal(99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(sucursalRepository, never()).deleteById(any());
    }
}
