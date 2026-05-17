package com.prueba.ms_vehiculos.runner;

import com.prueba.ms_vehiculos.model.Categoria;
import com.prueba.ms_vehiculos.model.Vehiculo;
import com.prueba.ms_vehiculos.repository.CategoriaRepository;
import com.prueba.ms_vehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class VehiculoRunner implements CommandLineRunner {

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    VehiculoRepository vehiculoRepository;

    @Override
    public void run(String... args) throws Exception {

        // CATEGORIA 1
        if (!categoriaRepository.existsById(1)) {
            categoriaRepository.save(
                    new Categoria(
                            null,
                            "SUV",
                            7,
                            true,
                            LocalDate.now(),
                            50000.0,
                            null
                    )
            );
        }

        // CATEGORIA 2
        if (!categoriaRepository.existsById(2)) {
            categoriaRepository.save(
                    new Categoria(
                            null,
                            "Sedan",
                            5,
                            true,
                            LocalDate.now(),
                            35000.0,
                            null
                    )
            );
        }

        // CATEGORIA 3
        if (!categoriaRepository.existsById(3)) {
            categoriaRepository.save(
                    new Categoria(
                            null,
                            "Camioneta",
                            2,
                            true,
                            LocalDate.now(),
                            65000.0,
                            null
                    )
            );
        }

        // OBTENER CATEGORIAS
        Categoria suv = categoriaRepository.findById(1)
                .orElse(null);
        Categoria sedan = categoriaRepository.findById(2)
                .orElse(null);
        Categoria camioneta = categoriaRepository.findById(3)
                .orElse(null);

        // VEHICULO 1
        if (!vehiculoRepository.existsById(1)) {

            Vehiculo vehiculo1 = new Vehiculo();
            vehiculo1.setPatente("ABCD12");
            vehiculo1.setMarca("Toyota");
            vehiculo1.setModelo("RAV4");
            vehiculo1.setPrecioDiario(85000.0);
            vehiculo1.setAnio(2024);
            vehiculo1.setDisponible(true);
            vehiculo1.setFechaIngreso(LocalDate.now());

            // RELACION MANY TO ONE
            vehiculo1.setCategoria(suv);
            vehiculoRepository.save(vehiculo1);
        }

        // VEHICULO 2
        if (!vehiculoRepository.existsById(2)) {

            Vehiculo vehiculo2 = new Vehiculo();
            vehiculo2.setPatente("EFGH34");
            vehiculo2.setMarca("Hyundai");
            vehiculo2.setModelo("Accent");
            vehiculo2.setPrecioDiario(45000.0);
            vehiculo2.setAnio(2023);
            vehiculo2.setDisponible(true);
            vehiculo2.setFechaIngreso(LocalDate.now());

            // RELACION MANY TO ONE
            vehiculo2.setCategoria(sedan);
            vehiculoRepository.save(vehiculo2);
        }

        // VEHICULO 3
        if (!vehiculoRepository.existsById(3)) {

            Vehiculo vehiculo3 = new Vehiculo();

            vehiculo3.setPatente("IJKL56");
            vehiculo3.setMarca("Ford");
            vehiculo3.setModelo("Ranger");
            vehiculo3.setPrecioDiario(95000.0);
            vehiculo3.setAnio(2025);
            vehiculo3.setDisponible(false);
            vehiculo3.setFechaIngreso(LocalDate.now());

            // RELACION MANY TO ONE
            vehiculo3.setCategoria(camioneta);
            vehiculoRepository.save(vehiculo3);
        }
        System.out.println(
                "✅ Datos iniciales de Vehiculos y Categorias cargados correctamente"
        );
    }
}
