package com.prueba.ms_sucursales.runner;

import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.repository.RegionRepository;
import com.prueba.ms_sucursales.repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Carga de datos iniciales, solo activa en el perfil "dev".
 */
@Component
@Profile("dev")
public class SucursalRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SucursalRunner.class);

    private final RegionRepository regionRepository;
    private final SucursalRepository sucursalRepository;

    public SucursalRunner(RegionRepository regionRepository, SucursalRepository sucursalRepository) {
        this.regionRepository = regionRepository;
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public void run(String... args) {
        cargarRegiones();
        cargarSucursales();
        log.info("Datos iniciales de ms-sucursales cargados correctamente");
    }

    private void cargarRegiones() {
        crearRegionSiNoExiste(1, "Metropolitana", "RM", true);
        crearRegionSiNoExiste(2, "Valparaiso", "V", true);
        crearRegionSiNoExiste(3, "Biobio", "VIII", true);
        crearRegionSiNoExiste(4, "Antofagasta", "II", true);
        crearRegionSiNoExiste(5, "La Araucania", "IX", true);
    }

    private void crearRegionSiNoExiste(Integer id, String nombre, String codigo, boolean activa) {
        if (!regionRepository.existsById(id)) {
            Region region = new Region();
            region.setNombre(nombre);
            region.setCodigo(codigo);
            region.setActiva(activa);
            regionRepository.save(region);
            log.debug("Region '{}' creada", nombre);
        }
    }

    private void cargarSucursales() {
        crearSucursalSiNoExiste(1, "Sucursal Santiago", "Alameda 123", "987654321", "Santiago", true, 20, 1);
        crearSucursalSiNoExiste(2, "Sucursal Viña del Mar", "Av Libertad 456", "912345678", "Viña del Mar", true, 15, 2);
        crearSucursalSiNoExiste(3, "Sucursal Concepcion", "O'Higgins 789", "923456789", "Concepcion", false, 10, 3);
        crearSucursalSiNoExiste(4, "Sucursal Antofagasta", "Av Angamos 321", "934567890", "Antofagasta", true, 12, 4);
        crearSucursalSiNoExiste(5, "Sucursal Temuco", "Av Alemania 654", "945678901", "Temuco", true, 8, 5);
    }

    private void crearSucursalSiNoExiste(Integer id, String nombre, String direccion, String telefono,
                                         String ciudad, boolean activa, int cantidadVehiculos, Integer regionId) {
        if (!sucursalRepository.existsById(id)) {
            Region region = regionRepository.findById(regionId).orElse(null);
            Sucursal sucursal = new Sucursal();
            sucursal.setNombre(nombre);
            sucursal.setDireccion(direccion);
            sucursal.setTelefono(telefono);
            sucursal.setCiudad(ciudad);
            sucursal.setActiva(activa);
            sucursal.setCantidadVehiculos(cantidadVehiculos);
            sucursal.setRegion(region);
            sucursalRepository.save(sucursal);
            log.debug("Sucursal '{}' creada", nombre);
        }
    }
}
