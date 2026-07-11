package com.prueba.ms_sucursales.service;

import com.prueba.ms_sucursales.dto.request.RegionRequestDTO;
import com.prueba.ms_sucursales.exception.ResourceNotFoundException;
import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.repository.RegionRepository;
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
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    private Region region;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setId(1);
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setActiva(true);
    }

    @Test
    void debeRetornarListaDeRegiones() {
        when(regionRepository.findAll()).thenReturn(List.of(region));

        List<Region> resultado = regionService.obtenerRegiones();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("RM");
    }

    @Test
    void debeRetornarRegionPorId() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(region));

        Region resultado = regionService.obtenerRegionPorId(1);

        assertThat(resultado.getNombre()).isEqualTo("Metropolitana");
    }

    @Test
    void debeLanzarExcepcionSiRegionNoExiste() {
        when(regionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> regionService.obtenerRegionPorId(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void debeCrearUnaRegion() {
        RegionRequestDTO dto = new RegionRequestDTO("Valparaiso", "V", true);
        when(regionRepository.save(any(Region.class))).thenReturn(region);

        Region resultado = regionService.guardarRegion(dto);

        assertThat(resultado).isNotNull();
        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void debeEliminarUnaRegionExistente() {
        when(regionRepository.existsById(1)).thenReturn(true);

        regionService.eliminarRegion(1);

        verify(regionRepository, times(1)).deleteById(1);
    }

    @Test
    void debeLanzarExcepcionAlEliminarRegionInexistente() {
        when(regionRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> regionService.eliminarRegion(99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(regionRepository, never()).deleteById(any());
    }
}
