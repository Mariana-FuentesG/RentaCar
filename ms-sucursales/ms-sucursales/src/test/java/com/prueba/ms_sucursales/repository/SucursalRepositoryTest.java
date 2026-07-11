package com.prueba.ms_sucursales.repository;

import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SucursalRepositoryTest {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private RegionRepository regionRepository;

    private Region region;

    @BeforeEach
    void setUp() {
        sucursalRepository.deleteAll();
        regionRepository.deleteAll();

        region = new Region();
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setActiva(true);
        region = regionRepository.save(region);
    }

    @Test
    void debeGuardarYRecuperarUnaSucursal() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal Santiago");
        sucursal.setDireccion("Alameda 123");
        sucursal.setTelefono("987654321");
        sucursal.setCiudad("Santiago");
        sucursal.setActiva(true);
        sucursal.setCantidadVehiculos(20);
        sucursal.setRegion(region);

        Sucursal guardada = sucursalRepository.save(sucursal);

        assertThat(guardada.getId()).isNotNull();
        assertThat(sucursalRepository.findById(guardada.getId())).isPresent();
    }

    @Test
    void debeListarSoloSucursalesOperativas() {
        Sucursal activa = crearSucursal("Activa", true, 10);
        Sucursal inactiva = crearSucursal("Inactiva", false, 5);
        sucursalRepository.save(activa);
        sucursalRepository.save(inactiva);

        List<Sucursal> operativas = sucursalRepository.obtenerSucursalesOperativas();

        assertThat(operativas).extracting(Sucursal::getNombre).containsExactly("Activa");
    }

    @Test
    void debeEliminarUnaSucursal() {
        Sucursal sucursal = sucursalRepository.save(crearSucursal("Temporal", true, 3));

        sucursalRepository.deleteById(sucursal.getId());

        assertThat(sucursalRepository.existsById(sucursal.getId())).isFalse();
    }

    private Sucursal crearSucursal(String nombre, boolean activa, int vehiculos) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(nombre);
        sucursal.setDireccion("Direccion " + nombre);
        sucursal.setTelefono("900000000");
        sucursal.setCiudad("Santiago");
        sucursal.setActiva(activa);
        sucursal.setCantidadVehiculos(vehiculos);
        sucursal.setRegion(region);
        return sucursal;
    }
}
