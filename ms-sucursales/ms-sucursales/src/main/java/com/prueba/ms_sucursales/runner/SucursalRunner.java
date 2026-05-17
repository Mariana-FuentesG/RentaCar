package com.prueba.ms_sucursales.runner;

import com.prueba.ms_sucursales.model.Region;
import com.prueba.ms_sucursales.model.Sucursal;
import com.prueba.ms_sucursales.repository.RegionRepository;
import com.prueba.ms_sucursales.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SucursalRunner implements CommandLineRunner {
    @Autowired
    RegionRepository regionRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Override
    public void run(String... args) {
        // REGIONES
        if(!regionRepository.existsById(1)) {
            Region region1 = new Region();
            region1.setNombre("Metropolitana");
            region1.setCodigo("RM");
            region1.setActiva(true);
            regionRepository.save(region1);
        }
        if(!regionRepository.existsById(2)) {
            Region region2 = new Region();
            region2.setNombre("Valparaiso");
            region2.setCodigo("V");
            region2.setActiva(true);
            regionRepository.save(region2);
        }
        if(!regionRepository.existsById(3)) {
            Region region3 = new Region();
            region3.setNombre("Biobio");
            region3.setCodigo("VIII");
            region3.setActiva(true);
            regionRepository.save(region3);
        }

        // SUCURSALES
        if(!sucursalRepository.existsById(1)) {

            Region region = regionRepository.findById(1).orElse(null);
            Sucursal sucursal1 = new Sucursal();
            sucursal1.setNombre("Sucursal Santiago");
            sucursal1.setDireccion("Alameda 123");
            sucursal1.setTelefono("987654321");
            sucursal1.setCiudad("Santiago");
            sucursal1.setActiva(true);
            sucursal1.setCantidadVehiculos(20);
            sucursal1.setRegion(region);
            sucursalRepository.save(sucursal1);
        }

        if(!sucursalRepository.existsById(2)) {

            Region region = regionRepository.findById(2).orElse(null);
            Sucursal sucursal2 = new Sucursal();
            sucursal2.setNombre("Sucursal Viña");
            sucursal2.setDireccion("Av Libertad 456");
            sucursal2.setTelefono("912345678");
            sucursal2.setCiudad("Viña del Mar");
            sucursal2.setActiva(true);
            sucursal2.setCantidadVehiculos(15);
            sucursal2.setRegion(region);
            sucursalRepository.save(sucursal2);
        }

        if(!sucursalRepository.existsById(3)) {
            Region region = regionRepository.findById(3).orElse(null);
            Sucursal sucursal3 = new Sucursal();
            sucursal3.setNombre("Sucursal Concepcion");
            sucursal3.setDireccion("O'Higgins 789");
            sucursal3.setTelefono("923456789");
            sucursal3.setCiudad("Concepcion");
            sucursal3.setActiva(false);
            sucursal3.setCantidadVehiculos(10);
            sucursal3.setRegion(region);
            sucursalRepository.save(sucursal3);
        }
        System.out.println(
                "✅ Datos iniciales cargados exitosamente");
    }
}
