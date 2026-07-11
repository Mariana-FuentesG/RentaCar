package com.prueba.ms_sucursales.repository;

import com.prueba.ms_sucursales.model.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @BeforeEach
    void setUp() {
        regionRepository.deleteAll();
    }

    @Test
    void debeGuardarYRecuperarUnaRegion() {
        Region region = new Region();
        region.setNombre("Valparaiso");
        region.setCodigo("V");
        region.setActiva(true);

        Region guardada = regionRepository.save(region);

        assertThat(guardada.getId()).isNotNull();
        assertThat(regionRepository.findById(guardada.getId())).isPresent();
    }

    @Test
    void debeBuscarPorNombreIgnorandoMayusculas() {
        Region region = new Region();
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setActiva(true);
        regionRepository.save(region);

        List<Region> encontradas = regionRepository.findByNombreContainingIgnoreCase("metro");

        assertThat(encontradas).hasSize(1);
        assertThat(encontradas.get(0).getNombre()).isEqualTo("Metropolitana");
    }

    @Test
    void debeEliminarUnaRegion() {
        Region region = new Region();
        region.setNombre("Temporal");
        region.setCodigo("TMP");
        region.setActiva(true);
        Region guardada = regionRepository.save(region);

        regionRepository.deleteById(guardada.getId());

        assertThat(regionRepository.existsById(guardada.getId())).isFalse();
    }
}
