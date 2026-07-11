package com.prueba.ms_reportes.service;

import com.prueba.ms_reportes.client.PagoClient;
import com.prueba.ms_reportes.client.ReservaClient;
import com.prueba.ms_reportes.dto.request.ReporteRequestDTO;
import com.prueba.ms_reportes.exception.ResourceNotFoundException;
import com.prueba.ms_reportes.model.Reporte;
import com.prueba.ms_reportes.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private PagoClient pagoClient;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;

    @BeforeEach
    void setUp() {
        reporte = new Reporte();
        reporte.setId(1);
        reporte.setTitulo("Reporte Mensual");
        reporte.setDescripcion("Reporte mensual de reservas");
        reporte.setTotalReservas(25);
        reporte.setTotalIngresos(5500000.0);
        reporte.setActivo(true);
        reporte.setFechaGeneracion(LocalDate.now());
    }

    @Test
    void debeRetornarListaDeReportes() {
        when(reporteRepository.findAll()).thenReturn(List.of(reporte));

        List<Reporte> resultado = reporteService.obtenerReportes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Reporte Mensual");
    }

    @Test
    void debeRetornarReportePorId() {
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporte));

        Reporte resultado = reporteService.obtenerReportePorId(1);

        assertThat(resultado.getId()).isEqualTo(1);
    }

    @Test
    void debeLanzarExcepcionSiReporteNoExiste() {
        when(reporteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.obtenerReportePorId(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void debeCrearUnReporte() {
        ReporteRequestDTO dto = new ReporteRequestDTO(
                "Reporte Nuevo", "Descripcion valida", 5, 100000.0, true, LocalDate.now());
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        Reporte resultado = reporteService.guardarReporte(dto);

        assertThat(resultado).isNotNull();
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    void debeEliminarUnReporteExistente() {
        when(reporteRepository.existsById(1)).thenReturn(true);

        reporteService.eliminarReporte(1);

        verify(reporteRepository, times(1)).deleteById(1);
    }

    @Test
    void debeLanzarExcepcionAlEliminarReporteInexistente() {
        when(reporteRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> reporteService.eliminarReporte(99))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(reporteRepository, never()).deleteById(any());
    }

    @Test
    void debeDelegarObtencionDeReservasAlFeignClient() {
        when(reservaClient.obtenerReservas()).thenReturn(List.of());

        reporteService.obtenerReservas();

        verify(reservaClient, times(1)).obtenerReservas();
    }

    @Test
    void debeDelegarObtencionDePagosAlFeignClient() {
        when(pagoClient.obtenerPagos()).thenReturn(List.of());

        reporteService.obtenerPagos();

        verify(pagoClient, times(1)).obtenerPagos();
    }
}
