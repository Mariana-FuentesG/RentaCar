package com.prueba.ms_reportes.repository;

import com.prueba.ms_reportes.model.Reporte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReporteRepositoryTest {

    @Autowired
    private ReporteRepository reporteRepository;

    @BeforeEach
    void setUp() {
        reporteRepository.deleteAll();
    }

    @Test
    void debeGuardarYRecuperarUnReporte() {
        Reporte reporte = crearReporte("Reporte Mensual", true);

        Reporte guardado = reporteRepository.save(reporte);

        assertThat(guardado.getId()).isNotNull();
        assertThat(reporteRepository.findById(guardado.getId())).isPresent();
    }

    @Test
    void debeListarSoloReportesActivos() {
        reporteRepository.save(crearReporte("Activo", true));
        reporteRepository.save(crearReporte("Inactivo", false));

        List<Reporte> activos = reporteRepository.findByActivoTrue();

        assertThat(activos).extracting(Reporte::getTitulo).containsExactly("Activo");
    }

    @Test
    void debeEliminarUnReporte() {
        Reporte guardado = reporteRepository.save(crearReporte("Temporal", true));

        reporteRepository.deleteById(guardado.getId());

        assertThat(reporteRepository.existsById(guardado.getId())).isFalse();
    }

    private Reporte crearReporte(String titulo, boolean activo) {
        Reporte reporte = new Reporte();
        reporte.setTitulo(titulo);
        reporte.setDescripcion("Descripcion de " + titulo);
        reporte.setTotalReservas(10);
        reporte.setTotalIngresos(1000000.0);
        reporte.setActivo(activo);
        reporte.setFechaGeneracion(LocalDate.now());
        return reporte;
    }
}
